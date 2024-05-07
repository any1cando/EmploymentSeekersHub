package com.panevrn.employmentseekershub.fragmentsMain

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.panevrn.employmentseekershub.ApiClient
import com.panevrn.employmentseekershub.MainActivity
import com.panevrn.employmentseekershub.R
import com.panevrn.employmentseekershub.SessionManager
import com.panevrn.employmentseekershub.VacancyAdapter
import com.panevrn.employmentseekershub.VacancyDetailed
import com.panevrn.employmentseekershub.model.dto.VacancyDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecyclerFragment: Fragment() {
    private lateinit var sessionManager: SessionManager
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterRecyclerView: VacancyAdapter
    private lateinit var vacanciesList: List<VacancyDto>
    private lateinit var apiClient: ApiClient
    private lateinit var imageViewAccount: ImageView  // ?? Чето придумать с иконкой и ее логикой в коде
    private var lastScrollPosition = 0  // Объявление переменной на уровне класса

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recycler, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())  // requireContext - фича фрагмента, чтобы получить контекст
        apiClient = ApiClient()
        recyclerView = view.findViewById(R.id.mainRecyclerView)


        apiClient.getVacancyService().getVacancies().enqueue(object: Callback<List<VacancyDto>> {
            override fun onResponse(
                call: Call<List<VacancyDto>>,
                response: Response<List<VacancyDto>>
            ) {
                Log.i("Status:", "onResponse is working")
                if (response.isSuccessful) {
                    vacanciesList = response.body()!!
                    Log.i("Vacancies", response.body().toString())
                    adapterRecyclerView = VacancyAdapter(
                        vacanciesList,
                        { vacancy -> handleLikeClicked(vacancy) },
                        { vacancy ->
                            val intent = Intent(view.context, VacancyDetailed::class.java)
                            intent.putExtra("vacancy_id", vacancy.id)
                            startActivity(intent)
                        }
                    )
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = adapterRecyclerView
                    }
                } else {
                    when (response.code()) {
                        400 -> {
                            val errorBodyRequest = response.errorBody()?.string()
                            Log.i("Error 400", errorBodyRequest.toString())
                            Toast.makeText(requireContext(), "Error 400", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            val errorBodyServer = response.errorBody()?.string()  // ошибки 500
                            Log.e("Error 500", response.message())
                            Toast.makeText(requireContext(), "Error 500", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<VacancyDto>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

        imageViewAccount = view.findViewById(R.id.imageViewAvatarMain)
        imageViewAccount.setImageResource(R.drawable.account_icon_default)

        searchView = view.findViewById(R.id.searchViewMain)
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

        })

    }


    override fun onPause() {
        super.onPause()
        // Сохранение состояния
        lastScrollPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    }

    override fun onResume() {
        super.onResume()
        recyclerView.post {
            recyclerView.layoutManager?.scrollToPosition(lastScrollPosition)
        }
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
        val filtredList = vacanciesList.filter { vacancy ->
            vacancy.vacancyTitle.contains(query, ignoreCase = true)
        }
        (recyclerView.adapter as? VacancyAdapter)?.updateData(filtredList)
    }

    private fun handleLikeClicked(vacancy: VacancyDto) {
        // Изменить состояние лайка в модели данных
        val index = vacanciesList.indexOf(vacancy)  // Используйте актуальный список адаптера
        if (index != -1) {
            vacanciesList[index].isLiked = !vacanciesList[index].isLiked
            adapterRecyclerView.notifyItemChanged(index)  // Обновляем конкретный элемент
        }

        // TODO: Отправить изменение состояния лайка на сервер или в базу данных
    }
}