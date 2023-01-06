package com.letsplay.security

data class LoginRequest(
    val provider: String,
    val identity: String?,
    val credential: String
)
