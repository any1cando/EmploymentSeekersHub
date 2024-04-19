package com.panevrn.employmentseekershub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.panevrn.employmentseekershub.model.dto.UserAuthorizationRequest
import com.panevrn.employmentseekershub.model.dto.UserTokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EnterActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var userLogin: EditText
    private lateinit var userPassword: EditText
    private lateinit var textViewLinkGoToRegistration: TextView
    private lateinit var buttonGoToMain: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter)

        sessionManager = SessionManager(this)
        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.my_ip_home_network_2_4g))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val authService = retrofit.create(AuthService::class.java)

        buttonGoToMain = findViewById(R.id.authorizationButton)
        // Настройка TextView как ссылки
        textViewLinkGoToRegistration = findViewById(R.id.registrationTextView)
        val contentTextView = SpannableString("Sign up!").apply {
            setSpan(UnderlineSpan(), 0, length, 0)
        }
        textViewLinkGoToRegistration.text = contentTextView


        textViewLinkGoToRegistration.setOnClickListener {  // Обработчик на переход к регистрации. Передаваемые поля - нет
            Toast.makeText(this@EnterActivity, "Coming to registration", Toast.LENGTH_SHORT).show()  // Вспылвающее предупреждение
            val intentToRegistration = Intent(this, RegistrationActivity::class.java)
            startActivity(intentToRegistration)
        }

        buttonGoToMain.setOnClickListener {  // Обработчик на авторизацию аккаунта. Передаваемые поля - логин(email) и пароль.
            userLogin = findViewById(R.id.loginEditText)
            userPassword = findViewById(R.id.passwordEditText)
            authLoginRequest(authService, userLogin.text.toString().trim(), userPassword.text.toString().trim())
        }

    }


    private fun authLoginRequest(authService: AuthService, userLogin: String, userPassword: String) {
        val loginInfo = UserAuthorizationRequest(email = userLogin, password = userPassword)
        authService.performLogin(loginInfo).enqueue(object : Callback<UserTokenResponse> {
            override fun onResponse(call: Call<UserTokenResponse>, response: Response<UserTokenResponse>) {
                Log.i("Status:", "onResponse is working")
                if (response.isSuccessful) {
                    // Обработка успешного ответа
                    val userResponse = response.body()
                    val accessToken = userResponse?.accessToken
                    val refreshToken = userResponse?.refreshToken
                    // Используйте токен по своему усмотрению
                    Log.i("Correct refresh token:", refreshToken.toString())
                    Log.i("Correct access token:", accessToken.toString())

                    sessionManager.saveAccessToken(accessToken)
                    sessionManager.saveRefreshToken(refreshToken)
                    Toast.makeText(this@EnterActivity, "Coming to main...", Toast.LENGTH_SHORT).show()
                    val intentToMain = Intent(this@EnterActivity, MainActivity::class.java)
                    startActivity(intentToMain)
                } else {
                    when (response.code()) {
                        400 -> {
                            val errorBodyRequest = response.errorBody()?.string()
                            Log.i("Error 400", errorBodyRequest.toString())
                            Toast.makeText(this@EnterActivity, "Error 400", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            val errorBodyServer = response.errorBody()?.string()  // ошибки 500
                            Log.i("Error 500", errorBodyServer.toString())
                            Toast.makeText(this@EnterActivity, "Error 500", Toast.LENGTH_SHORT).show()
                        }
                    }
                    // Обработка ошибок, например, неправильные учетные данные
                }
            }

            override fun onFailure(call: Call<UserTokenResponse>, t: Throwable) {
                val error: String = t.message.toString()
                Log.e("Else error", error)
                Toast.makeText(this@EnterActivity, "Something went wrong...", Toast.LENGTH_SHORT).show()
            }
        })
    }

}