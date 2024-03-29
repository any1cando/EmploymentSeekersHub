package com.panevrn.employmentseekershub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import com.panevrn.employmentseekershub.model.dto.RegistrationPersonStatus
import com.panevrn.employmentseekershub.model.dto.RegistrationPersonStatusAdapter
import com.panevrn.employmentseekershub.model.dto.UserRegistrationRequest
import com.panevrn.employmentseekershub.model.dto.UserTokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class RegistrationActivity : AppCompatActivity() {
    private lateinit var userRoleItemElement: RegistrationPersonStatus
    private lateinit var userRoleRequest: String
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var userLogin: EditText
    private lateinit var userPassword: EditText
    private val retrofit = Retrofit.Builder()
        .baseUrl(R.string.my_ip.toString())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val authService = retrofit.create(AuthService::class.java)
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val registrationButton: Button = findViewById<Button>(R.id.confirmRegistrationButton)
        val backToEnterButton: ImageButton =
            findViewById<ImageButton>(R.id.backFromRegistrationToEnterButton)

        // Установка адаптера на спиннер и его первоначальная настройка
        val spinnerRegistrationChoices = listOf(
            RegistrationPersonStatus("Part of a team"),
            RegistrationPersonStatus("Solo creator")
        )
        val adapterSpinner = RegistrationPersonStatusAdapter(
            this,
            android.R.layout.simple_spinner_item,
            spinnerRegistrationChoices
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)  // Настроили прокрутку спиннера.
        spinner = findViewById(R.id.registrationSpinner)
        spinner.adapter = adapterSpinner

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                userRoleItemElement =
                    parent.getItemAtPosition(position) as RegistrationPersonStatus
                userRoleRequest = if (userRoleItemElement.equals("Part of a team")) {
                    "APPLICANT"
                } else {
                    "COMPANY_OWNER"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Ничего не делать, если ничего не выбрано
            }
        }

        registrationButton.setOnClickListener {   // Обработчик при регистрации
            firstName = findViewById(R.id.firstNameUserRegistrationEditText)
            lastName = findViewById(R.id.lastNameUserRegistrationEditText)
            userLogin = findViewById(R.id.emailUserRegistrationEditText)
            userPassword = findViewById(R.id.passwordUserRegistrationEditText)

            if (!isValidEmail(userLogin.text.toString().trim())) {
                userLogin.error = "Invalid email format"
            } else if (!isValidPassword(userPassword.text.toString().trim())) {
                userPassword.error = "Password is weak"
            } else {
                authRegistrationRequest(
                    authService,
                    firstName.text.toString().trim(),
                    lastName.text.toString().trim(),
                    userLogin.text.toString().trim(),
                    userPassword.text.toString().trim(),
                    userRoleRequest
                )
            }
        }

        backToEnterButton.setOnClickListener {  // Обработчик при возвращении без регистрации
            val intentToEnter = Intent(this, EnterActivity::class.java)
            print("Вернулся без регистрации")
            startActivity(intentToEnter)
        }

    }


    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    private fun isValidPassword(password: CharSequence): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
        val pattern = Pattern.compile(passwordPattern)
        return pattern.matcher(password).matches()
    }


    private fun authRegistrationRequest(
        authService: AuthService,
        firstName: String,
        lastName: String,
        userLogin: String,
        userPassword: String,
        userRole: String
    ) {
//        val firstName: String = findViewById<EditText>(R.id.firstNameUserRegistrationEditText).text.toString()
//        val lastName: String = findViewById<EditText>(R.id.lastNameUserRegistrationEditText).text.toString()
//        val userLogin: String = findViewById<EditText>(R.id.emailUserRegistrationEditText).text.toString()
//        val userPassword: String = findViewById<EditText>(R.id.passwordUserRegistrationEditText).text.toString()

        val registrationInfo = UserRegistrationRequest(
            firstName = firstName,
            lastName = lastName,
            email = userLogin,
            password = userPassword,
            userRole = userRole
        )
        authService.performRegistration(registrationInfo).enqueue(object : Callback<UserTokenResponse> {
                override fun onResponse(
                    call: Call<UserTokenResponse>,
                    response: Response<UserTokenResponse>) {
                    Log.i("onResponse", "ВСЁ КОРРЕКТНО")
                    if (response.isSuccessful) {
                        // Обработка успешного ответа
                        val userResponse = response.body()
                        val accessToken = userResponse?.accessToken
                        val refreshToken = userResponse?.refreshToken
                        // Используйте токен по своему усмотрению
                        val intentToMain = Intent(
                            this@RegistrationActivity,
                            EnterActivity::class.java
                        )  // !!!!!!Также добавить переход putExtra для двух токенов или SharedPreferences
                        startActivity(intentToMain)
                    } else {
                        when (response.code()) {
                            400 -> {
                                val errorBodyRequest = response.errorBody()?.string()
                            }

                            else -> {
                                val errorBodyServer = response.errorBody()?.string()  // ошибки 500
                            }
                        }
                        // Обработка ошибок, например, неправильные учетные данные
                    }
                }

                override fun onFailure(call: Call<UserTokenResponse>, t: Throwable) {
                    val error: String = t.message.toString()
                    Log.i("onResponse", "ВСЁ ХУЕВО")
                    Log.i("Error", t.message.toString())
                }
            })
    }
}