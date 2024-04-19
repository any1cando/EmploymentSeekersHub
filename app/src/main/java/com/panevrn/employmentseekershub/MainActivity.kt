package com.panevrn.employmentseekershub

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.panevrn.employmentseekershub.model.dto.UserTokenResponse
import com.panevrn.employmentseekershub.model.dto.Vacancy
import kotlinx.coroutines.handleCoroutineException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterRecyclerView: VacancyAdapter
    private var testList: MutableList<Vacancy> = mutableListOf()

    private lateinit var imageViewAccount: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(this)
        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.my_ip_home_network_5g))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val authService = retrofit.create(AuthService::class.java)

        authService.performHello(token = "Bearer ${sessionManager.fetchAccessToken()}").enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.i("Status HelloCallback", response.body().toString())
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("Status HelloCallback", t.message.toString())
            }

        })
        testList = mutableListOf(
            Vacancy(
                id = "1",
                vacancyTitle = "Product Designer",
                companyTitle = "MetaMask",
                countCandidates = 25,
                isLiked = false,
                tags = listOf("Entry Level", "Full-Time"),
                description = "Innovate and design new user experiences.",
                salary = "$250/hr",
                postedTime = "12 days ago"
            ),
            Vacancy(
                id = "2",
                vacancyTitle = "Frontend Developer",
                companyTitle = "Decentraland",
                countCandidates = 40,
                isLiked = true,
                tags = listOf("Mid-Level", "Full-Time", "Remote"),
                description = "Develop cutting-edge web applications.",
                salary = "$300/hr",
                postedTime = "3 days ago"
            ),
            Vacancy(
                id = "3",
                vacancyTitle = "Blockchain Engineer",
                companyTitle = "Chainlink",
                countCandidates = 10,
                isLiked = false,
                tags = listOf("Senior Level", "Part-Time"),
                description = "Build decentralized networks.",
                salary = "$350/hr",
                postedTime = "5 days ago"
            ),
            Vacancy(
                id = "4",
                vacancyTitle = "UX Researcher",
                companyTitle = "Uniswap",
                countCandidates = 15,
                isLiked = false,
                tags = listOf("Entry Level", "Contract"),
                description = "Conduct user research and tests.",
                salary = "$150/hr",
                postedTime = "2 weeks ago"
            )
        )

        // Супер затычка на чек адаптера
        fun handleLikeClicked(vacancy: Vacancy) {
            // Изменить состояние лайка в модели данных
            val index = testList.indexOf(vacancy)
            if (index != -1) {
                testList[index].isLiked = !testList[index].isLiked
                // Обновление элемента в RecyclerView
                adapterRecyclerView.notifyItemChanged(index)
            }

            // TODO: Отправить изменение состояния лайка на сервер или в базу данных
        }
        adapterRecyclerView = VacancyAdapter(testList) {vacancy ->
            handleLikeClicked(vacancy)
        }
        recyclerView = findViewById(R.id.mainRecyclerView)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterRecyclerView
        }

        imageViewAccount = findViewById<ImageView>(R.id.imageViewAvatarMain)
        imageViewAccount.setImageResource(R.drawable.account_icon_default)  // Установка базовой картинки на профиль

        // Определение SearchView, настройка внешнего вида в виде функции, слушатель для поисковика
        searchView = findViewById(R.id.searchViewMain)
        changeSearchView(searchView)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchForJob(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchForJob(it) }
                return true
            }

        }
        )

    }

    // Функция, которая вызывается всегда при начале программы, чтобы изменить параметры searchView.
    private fun changeSearchView (objectView: SearchView) {
        objectView.queryHint = resources.getString(R.string.search_const)
        objectView.isIconified = true  // должен ли SearchView быть свернут в иконку поиска или расширен.
        objectView.isSubmitButtonEnabled = true  // Включает или отключает отображение кнопки отправки
        objectView.setBackgroundColor(resources.getColor(R.color.mainWhite))

        searchView.setIconifiedByDefault(false)
        searchView.clearFocus()
    }


    // Функция, которая используется в слушателе поисковика
    private fun searchForJob(query: String) {
        val filtredList = testList.filter { vacancy ->
            vacancy.vacancyTitle.contains(query, ignoreCase = true)
        }
        (recyclerView.adapter as VacancyAdapter).updateData(filtredList)
    }

}