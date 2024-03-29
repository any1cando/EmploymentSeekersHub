package com.panevrn.employmentseekershub.model.dto

data class UserAuthorizationRequest(
    val email: String,
    val password: String
)