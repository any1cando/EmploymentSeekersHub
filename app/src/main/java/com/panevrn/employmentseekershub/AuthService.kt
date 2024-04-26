package com.panevrn.employmentseekershub

import com.panevrn.employmentseekershub.model.dto.UserAuthorizationRequest
import com.panevrn.employmentseekershub.model.dto.UserRegistrationRequest
import com.panevrn.employmentseekershub.model.dto.UserTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("v1/auth/login")  // Строка для ввода адреса
    fun performLogin(@Body loginInfo: UserAuthorizationRequest): Call<UserTokenResponse>  // Метод для выполнения запроса на сервер (передача логин/пароль)

    @POST("v1/auth/registration")  // Строка для ввода регистрации
    fun performRegistration(@Body registrationInfo: UserRegistrationRequest): Call<UserTokenResponse>

    @GET("v1/greet/hello")
    fun performHello(@Header("Authorization") token: String): Call<String>
}
