package com.jesando.zphererent.model.response.auth

data class VerifyTokenResponse(
    val error: Boolean,
    val success: Int,
    val message: String,
    val user: User?,
    val token: String?
)


