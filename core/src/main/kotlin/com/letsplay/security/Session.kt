package com.letsplay.security

data class Session(
    val sessionId: String,
    val userId: String,
    val token: String,
)
