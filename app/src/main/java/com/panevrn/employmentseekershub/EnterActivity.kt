package com.panevrn.employmentseekershub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.panevrn.employmentseekershub.model.dto.UserAuthorizationRequest
import com.panevrn.employmentseekershub.model.dto.UserTokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern
import kotlin.reflect.typeOf

class EnterActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    // private lateinit var buttonGoToRegistration: Button
    private lateinit var userLogin: EditText
    private lateinit var userPassword: EditText
    private lateinit var textViewLinkGoToRegistration: TextView
    private lateinit var buttonGoToMain: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter)

        sessionManager = SessionManager(this)
        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.my_ip))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val authService = retrofit.create(AuthService::class.java)

        Log.i("StatusSpec", R.string.my_ip.toString())
        textViewLinkGoToRegistration = findViewById(R.id.registrationTextView)
        val contentTextView = SpannableString("Sign up!").apply {  // Настройка TextView как ссылки
            setSpan(UnderlineSpan(), 0, length, 0)
        }
        textViewLinkGoToRegistration.text = contentTextView

        // buttonGoToRegistration = findViewById(R.id.registrationButton)
        buttonGoToMain = findViewById(R.id.authorizationButton)

//        val login: String = findViewById<EditText>(R.id.loginEditText).text.toString()
//        val password: String = findViewById<EditText>(R.id.passwordEditText).text.toString()

        textViewLinkGoToRegistration.setOnClickListener {  // Обработчик на переход к регистрации. Передаваемые поля - нет
            Toast.makeText(this, "Coming to registration", Toast.LENGTH_SHORT).show()  // Вспылвающее предупреждение
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
                    val intentToMain = Intent(this@EnterActivity, MainActivity::class.java)  // !!!!!!Также добавить переход putExtra для двух токенов или SharedPreferences
                    startActivity(intentToMain)
                } else {
                    when (response.code()) {
                        400 -> {
                            val errorBodyRequest = response.errorBody()?.string()
                            Log.i("Error 400", errorBodyRequest.toString())
                        }
                        else -> {
                            val errorBodyServer = response.errorBody()?.string()  // ошибки 500
                            Log.i("Error 500", errorBodyServer.toString())
                        }
                    }
                    // Обработка ошибок, например, неправильные учетные данные
                }
            }

            override fun onFailure(call: Call<UserTokenResponse>, t: Throwable) {
                val error: String = t.message.toString()
                Log.e("Error", error)
            }
        })
    }

}