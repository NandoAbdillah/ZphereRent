package com.jesando.zphererent.model

data class ForgotPasswordResponse(
    val error: Boolean,
    val success: Int,
    val message: String,
    val user: User?,
    val token: String?
)


