package com.panevrn.employmentseekershub.model.dto

data class Vacancy(
    val vacancyTitle: String, // Название вакансии
    val companyTitle: String, // Название компании
    val countCandidates: Int,  // Количество откликов ['applicants']
    val tags: List<String>, // Теги вакансии
    val description: String,  // Описание вакансии (будет ограничено по размеру)
    val salary: String,  // Зарплата
    val postedTime: String,  // Дата публикации вакансии
    var isLiked: Boolean,  // Стоит лайк или нет (пока в бете)
    val id: String
)


