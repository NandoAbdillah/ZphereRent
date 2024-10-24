package com.jesando.zphererent.model.response.auth

data class LoginResponse(
    val error: Boolean,
    val success: Int,
    val message: String,
    val user: User?,
    val token: String?
)


