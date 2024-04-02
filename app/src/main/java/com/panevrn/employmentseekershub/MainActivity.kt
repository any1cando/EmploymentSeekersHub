package com.panevrn.employmentseekershub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.panevrn.employmentseekershub.model.dto.UserTokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(this)
        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.my_ip))
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
    }
}