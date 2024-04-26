package com.panevrn.employmentseekershub


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.panevrn.employmentseekershub.model.dto.VacancyDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterRecyclerView: VacancyAdapter
    private var testList: MutableList<VacancyDto> = mutableListOf()
    private lateinit var apiClient: ApiClient
    private lateinit var navController: NavController  // Для регулировки фрагментов
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var imageViewAccount: ImageView  // ?? Чето придумать с иконкой и ее логикой в коде

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        navController = findNavController(R.id.nav_host_fragment)
//        bottomNavigationView = findViewById(R.id.bottom_navigation)
//        bottomNavigationView.setupWithNavController(navController)

//        sessionManager = SessionManager(this)
//        apiClient = ApiClient()
//        recyclerView = findViewById(R.id.mainRecyclerView)
//
//
//        apiClient.getVacancyService().getVacancies().enqueue(object: Callback<List<VacancyDto>> {
//            override fun onResponse(
//                call: Call<List<VacancyDto>>,
//                response: Response<List<VacancyDto>>
//            ) {
//                Log.i("Status:", "onResponse is working")
//                if (response.isSuccessful) {
//                    Log.i("Vacancies", response.body().toString())
//                    adapterRecyclerView = VacancyAdapter(response.body()) {vacancy ->
//                        handleLikeClicked(vacancy)
//                    }
//                    recyclerView.apply {
//                        layoutManager = LinearLayoutManager(this@MainActivity)
//                        adapter = adapterRecyclerView
//                    }
//                } else {
//                    when (response.code()) {
//                        400 -> {
//                            val errorBodyRequest = response.errorBody()?.string()
//                            Log.i("Error 400", errorBodyRequest.toString())
//                            Toast.makeText(this@MainActivity, "Error 400", Toast.LENGTH_SHORT).show()
//                        }
//                        else -> {
//                            val errorBodyServer = response.errorBody()?.string()  // ошибки 500
//                            Log.e("Error 500", response.message())
//                            Toast.makeText(this@MainActivity, "Error 500", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                    // Обработка ошибок, например, неправильные учетные данные
//                }
//            }
//
//            override fun onFailure(call: Call<List<VacancyDto>>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//
//        })
//
//        // Супер затычка на чек адаптера
//
//        imageViewAccount = findViewById(R.id.imageViewAvatarMain)
//        imageViewAccount.setImageResource(R.drawable.account_icon_default)  // Установка базовой картинки на профиль
//
//        // Определение SearchView, настройка внешнего вида в виде функции, слушатель для поисковика
//        searchView = findViewById(R.id.searchViewMain)
//        changeSearchView(searchView)
//        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                query?.let { searchForJob(it) }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                newText?.let { searchForJob(it) }
//                return true
//            }
//
//        }
//        )
//
//    }

        // Функция, которая вызывается всегда при начале программы, чтобы изменить параметры searchView.
//    private fun changeSearchView (objectView: SearchView) {
//        objectView.queryHint = resources.getString(R.string.search_const)
//        objectView.isIconified = true  // должен ли SearchView быть свернут в иконку поиска или расширен.
//        objectView.isSubmitButtonEnabled = true  // Включает или отключает отображение кнопки отправки
//        objectView.setBackgroundColor(resources.getColor(R.color.mainWhite))
//
//        searchView.setIconifiedByDefault(false)
//        searchView.clearFocus()
//    }
//
//
//    // Функция, которая используется в слушателе поисковика
//    private fun searchForJob(query: String) {
//        val filtredList = testList.filter { vacancy ->
//            vacancy.vacancyTitle.contains(query, ignoreCase = true)
//        }
//        (recyclerView.adapter as VacancyAdapter).updateData(filtredList)
//    }
//
//    private fun handleLikeClicked(vacancy: VacancyDto) {
//        // Изменить состояние лайка в модели данных
//        val index = testList.indexOf(vacancy)
//        if (index != -1) {
//            testList[index].isLiked = !testList[index].isLiked
//            // Обновление элемента в RecyclerView
//            adapterRecyclerView.notifyItemChanged(index)
//        }
//
//        // TODO: Отправить изменение состояния лайка на сервер или в базу данных
//    }

    }
}