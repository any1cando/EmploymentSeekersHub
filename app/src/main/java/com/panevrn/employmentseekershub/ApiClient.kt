package com.panevrn.employmentseekershub

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
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.64:8081/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            vacancyService = retrofit.create(VacancyService::class.java)
        }

        return vacancyService
    }

}

