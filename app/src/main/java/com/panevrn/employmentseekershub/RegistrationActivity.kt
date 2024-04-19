package com.panevrn.employmentseekershub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import com.panevrn.employmentseekershub.adapter.spinner.RegistrationPersonStatusAdapter
import com.panevrn.employmentseekershub.model.dto.RegistrationPersonStatus
import com.panevrn.employmentseekershub.model.dto.UserRegistrationRequest
import com.panevrn.employmentseekershub.model.dto.UserTokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class RegistrationActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var userRoleItemElement: RegistrationPersonStatus
    private lateinit var userRoleRequest: String
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var userLogin: EditText
    private lateinit var userPassword: EditText
    private lateinit var spinner: Spinner

    private var userHasInteracted = false

    override fun onUserInteraction() {
        super.onUserInteraction()
        userHasInteracted = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)


        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.my_ip_home_network_5g))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val authService = retrofit.create(AuthService::class.java)
        val registrationButton: Button = findViewById(R.id.confirmRegistrationButton)
        val backToEnterButton: ImageButton = findViewById(R.id.backFromRegistrationToEnterButton)

        // Установка адаптера на спиннер и его первоначальная настройка
        spinner = findViewById(R.id.registrationSpinner)
        val spinnerRegistrationChoices = mutableListOf(
            RegistrationPersonStatus("Choose a role:"),
            RegistrationPersonStatus(resources.getString(R.string.part_of_a_team)),
            RegistrationPersonStatus(resources.getString(R.string.solo_creator))
        )
        val adapterSpinner = RegistrationPersonStatusAdapter(
            this,
            android.R.layout.simple_spinner_item,
            spinnerRegistrationChoices
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)  // Настроили прокрутку спиннера.
        spinner.adapter = adapterSpinner

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (!userHasInteracted) {
                    return // Если это вызов при инициализации, не предпринимаем действий
                }

                // Обновляем выбранный элемент и его запрос
                userRoleItemElement = parent.getItemAtPosition(position) as RegistrationPersonStatus
                userRoleRequest = when (userRoleItemElement.status) {
                    resources.getString(R.string.part_of_a_team) -> "APPLICANT"
                    resources.getString(R.string.solo_creator) -> "COMPANY_OWNER"
                    else -> "NOTHING" // В случае, если выбрана заглушка, можно установить пустую строку или другое значение по умолчанию
                }

                // Удаляем заглушку "Choose a role:" после первого выбора пользователя
                if (spinnerRegistrationChoices[0].status == "Choose a role:") {
                    spinnerRegistrationChoices.removeAt(0) // Удаляем заглушку
                    (parent.adapter as ArrayAdapter<*>).notifyDataSetChanged() // Обновляем адаптер
                    spinner.setSelection(position-1) // Устанавливаем выбранный элемент, учитывая удаление заглушки
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                (spinner.adapter as RegistrationPersonStatusAdapter).isSelectionMade = false
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
                Toast.makeText(
                    this@RegistrationActivity,
                    "Password must contain at least 1 little, 1 big, 1 number and special symbol",
                    Toast.LENGTH_LONG).show()
            } else {
                authRegistrationRequest(
                    authService,
                    firstName.text.toString().trim(),
                    lastName.text.toString().trim(),
                    userLogin.text.toString().trim(),
                    userPassword.text.toString().trim(),
                    userRoleRequest
                )
                Toast.makeText(this@RegistrationActivity, "Profile has been created!", Toast.LENGTH_SHORT).show()
            }
        }

        backToEnterButton.setOnClickListener {  // Обработчик при возвращении без регистрации
            val intentToEnter = Intent(this, EnterActivity::class.java)
            Log.i("Status:", "Вернулся без регистрации")
            Toast.makeText(this@RegistrationActivity, "Coming to enter...", Toast.LENGTH_SHORT).show()
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

        val registrationInfo = UserRegistrationRequest(firstName = firstName, lastName = lastName, email = userLogin,
            password = userPassword, userRole = userRole)

        authService.performRegistration(registrationInfo).enqueue(object : Callback<UserTokenResponse> {
                override fun onResponse(call: Call<UserTokenResponse>, response: Response<UserTokenResponse>) {
                    Log.i("Status:", "OnResponse is working")
                    if (response.isSuccessful) {
                        // Обработка успешного ответа
                        val userResponse = response.body()
                        val accessToken = userResponse?.accessToken
                        val refreshToken = userResponse?.refreshToken

                        sessionManager.saveAccessToken(accessToken)
                        sessionManager.saveRefreshToken(refreshToken)

                        val intentToMain = Intent(this@RegistrationActivity, EnterActivity::class.java)

                        startActivity(intentToMain)
                    } else {
                        when (response.code()) {
                            400 -> {
                                val errorBodyRequest = response.errorBody()?.string()
                                Log.i("Error 400", errorBodyRequest.toString())
                                Toast.makeText(this@RegistrationActivity, "Error 400", Toast.LENGTH_SHORT).show()
                            }

                            else -> {
                                val errorBodyServer = response.errorBody()?.string()
                                Log.i("Error 500", errorBodyServer.toString())
                                Toast.makeText(this@RegistrationActivity, "Error 500", Toast.LENGTH_SHORT).show()
                            }
                        }
                        // Обработка ошибок, например, неправильные учетные данныx ???
                    }
                }

                override fun onFailure(call: Call<UserTokenResponse>, t: Throwable) {
                    Log.i("Status:", "OnResponse's fail")
                    Log.i("Error:", t.message.toString())
                    Toast.makeText(this@RegistrationActivity, "Something went wrong...", Toast.LENGTH_SHORT).show()
                }
            })
    }
}