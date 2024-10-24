package com.jesando.zphererent.model.response.auth

data class ResetPasswordResponse(
    val error: Boolean,
    val success: Int,
    val message: String,
    val user: User?,
    val token: String?
)


