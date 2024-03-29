package com.panevrn.employmentseekershub.model.dto

data class UserTokenResponse(
    val accessToken: String,
    val refreshToken: String
)