package com.panevrn.employmentseekershub.model.dto

data class VacancyDto(
    val id: String,
    val vacancyTitle: String, // Название вакансии
    val companyId: String,  // ID компании
    val companyTitle: String, // Название компании
    val countCandidates: Int,  // Количество откликов ['applicants']
    val tags: List<String>, // Теги вакансии
    val description: String,  // Описание вакансии (будет ограничено по размеру)
    val salary: SalaryDto,  // Зарплата
    val postedTime: String,  // Дата публикации вакансии
    var isLiked: Boolean,  // Стоит лайк или нет (пока в бете)
)


