package com.panevrn.employmentseekershub

import com.google.gson.GsonBuilder
import com.panevrn.employmentseekershub.model.dto.FilterData
import com.panevrn.employmentseekershub.model.dto.FilterDataDeserializer
import com.panevrn.employmentseekershub.model.dto.FiltersDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient() {
    private lateinit var authService: AuthService
    private lateinit var vacancyService: VacancyService

    fun getAuthService(): AuthService {

        if (!::authService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.64:8081/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            authService = retrofit.create(AuthService::class.java)
        }

        return authService
    }

    fun getVacancyService(): VacancyService {
        if (!::vacancyService.isInitialized) {
            val gson = GsonBuilder()
                .registerTypeAdapter(FiltersDto::class.java, FilterDataDeserializer())
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.64:8081/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            vacancyService = retrofit.create(VacancyService::class.java)
        }

        return vacancyService
    }

}

