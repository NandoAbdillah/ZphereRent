package com.jesando.zphererent.model.response.auth


data class User(
    val id: Int,
    val name: String,
    val email: String,
    val email_verified_at: String?,
    val profile: String?,
    val role: String,
    val created_at: String,
    val updated_at: String
)