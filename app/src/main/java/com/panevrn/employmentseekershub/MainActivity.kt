package com.panevrn.employmentseekershub

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.SearchView

import com.panevrn.employmentseekershub.model.dto.UserTokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var searchView: SearchView

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

        imageViewAccount = findViewById<ImageView>(R.id.imageViewAvatarMain)
        imageViewAccount.setImageResource(R.drawable.account_icon_default)  // Установка базовой картинки на профиль

        searchView = findViewById(R.id.searchViewMain)
        changeSearchView(searchView)

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

}