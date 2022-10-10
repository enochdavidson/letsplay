package com.letsplay.security.jwt

import org.springframework.security.core.AuthenticationException

class JwtException(message: String, e: Throwable?) : AuthenticationException(message, e) {
    constructor(message: String) : this(message, null)
}