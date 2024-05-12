package com.panevrn.employmentseekershub

import android.content.Intent
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.panevrn.employmentseekershub.model.dto.VacancyDto
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class VacancyDetailed : AppCompatActivity() {
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    lateinit var vacancyTitle: TextView
    lateinit var vacancyCompanyTitle: TextView
    lateinit var vacancySalary: TextView
    lateinit var vacancyApplicants: TextView
    lateinit var vacancyPostedTime: TextView
    lateinit var vacancyDescription: TextView
    lateinit var vacancyTags: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacancy_detailed)


        apiClient = ApiClient()
        sessionManager = SessionManager(this@VacancyDetailed)
        val accessToken = sessionManager.fetchAccessToken()
        val vacancyId = intent.getStringExtra("vacancy_id")

        vacancyTitle = findViewById(R.id.detailedVacancyTitle)
        vacancyCompanyTitle = findViewById(R.id.detailedCompanyTitle)
        vacancySalary = findViewById(R.id.detailedSalary)
        vacancyApplicants = findViewById(R.id.detailedApplicants)
        vacancyPostedTime = findViewById(R.id.detailedPostedTime)
        vacancyDescription = findViewById(R.id.detailedDescription)

        val buttonBackFromVacancyToRecycler = findViewById<ImageButton>(R.id.backFromVacancyToRecycler)
        val buttonApplyVacancy = findViewById<Button>(R.id.detailedApplyButton)
        val linearLayout: LinearLayout = findViewById(R.id.tagsDetailed)

        buttonBackFromVacancyToRecycler.setOnClickListener {
            finish()
        }

        getVacancy(apiClient, vacancyId!!, accessToken!!,
            onSuccess = { vacancy ->
                vacancyTitle.text = vacancy?.vacancyTitle
                vacancyCompanyTitle.text = vacancy?.companyTitle
                vacancySalary.text = vacancy?.salary?.amount.toString()
                vacancyApplicants.text = vacancy?.countCandidates.toString()
                vacancyPostedTime.text = vacancy?.postedTime.toString()
                vacancyDescription.text = vacancy?.description
                loadData(vacancy!!.tags, linearLayout)
            },
            onError = { error ->
                Toast.makeText(this@VacancyDetailed, error.message.toString(), Toast.LENGTH_SHORT).show()
            })
        

    }


    fun loadData(tags: List<String>, layout: LinearLayout) {

        layout.removeAllViews()

        tags.forEach { tag ->
            val tagView = TextView(this).apply {
                text = tag
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    rightMargin = 16.dp // Добавьте отступ справа для разделения тегов
                }
                setBackgroundResource(R.drawable.tag_background)
                setPadding(8.dp, 4.dp, 8.dp, 4.dp) // Настройка отступов внутри тега
                setTextColor(ContextCompat.getColor(context, R.color.mainWhite))
            }
            layout.addView(tagView)
        }

    }


    fun getVacancy(apiClient: ApiClient, vacancyId: String, token: String, onSuccess: (VacancyDto?) -> Unit, onError: (Throwable) -> Unit) {
        apiClient.getVacancyService().getVacancyById(vacancyId, "Bearer $token").enqueue(object: Callback<VacancyDto> {
            override fun onResponse(call: Call<VacancyDto>, response: Response<VacancyDto>) {
                if (response.isSuccessful) {
                    response.body().let {
                        onSuccess(it)
                    }
                } else {
                    val errorMessage = "Failed to load vacancy: ${response.errorBody()?.string() ?: "Unknown error"}"
                    Log.e("Error Personal Vacancy", errorMessage)
                    Toast.makeText(this@VacancyDetailed, errorMessage, Toast.LENGTH_SHORT).show()
                    onError(RuntimeException(errorMessage))
                }
            }

            override fun onFailure(call: Call<VacancyDto>, t: Throwable) {
                Log.e("Error Personal Vacancy", "Network error: ${t.message}", t)
                Toast.makeText(this@VacancyDetailed, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                onError(t)
            }

        })
    }


}