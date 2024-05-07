package com.panevrn.employmentseekershub

import android.content.Intent
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class VacancyDetailed : AppCompatActivity() {
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

        val vacancyId = intent.getStringExtra("vacancy_id")

        vacancyTitle = findViewById(R.id.detailedVacancyTitle)
        vacancyCompanyTitle = findViewById(R.id.detailedCompanyTitle)
        vacancySalary = findViewById(R.id.detailedSalary)
        vacancyApplicants = findViewById(R.id.detailedApplicants)
        vacancyPostedTime = findViewById(R.id.detailedPostedTime)
        vacancyDescription = findViewById(R.id.detailedDescription)
        vacancyTags = listOf("Good", "Nice Price", "The Best")

        val buttonBackFromVacancyToRecycler = findViewById<ImageButton>(R.id.backFromVacancyToRecycler)
        val buttonApplyVacancy = findViewById<Button>(R.id.detailedApplyButton)
        val linearLayout: LinearLayout = findViewById(R.id.tagsDetailed)

        buttonBackFromVacancyToRecycler.setOnClickListener {
            finish()
        }

        loadData(vacancyTags, linearLayout)

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
}