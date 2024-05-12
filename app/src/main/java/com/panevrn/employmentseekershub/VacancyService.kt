package com.panevrn.employmentseekershub

import com.panevrn.employmentseekershub.model.dto.VacancyDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface VacancyService {
    @GET("v1/vacancies")
    fun getVacancies(): Call<List<VacancyDto>>


    @GET("v1/vacancies/{vacancyId}")
    fun getVacancyById(@Path("vacancyId") id: String, @Header("Authorization") token: String): Call<VacancyDto>

}