package com.panevrn.employmentseekershub.model.dto

data class UserRegistrationRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val userRole: String
)
