package com.letsplay.security

import com.letsplay.user.UserIdentity
import reactor.core.publisher.Mono

interface AuthenticationProvider {

    val providerId: String

    fun authenticate(request: LoginRequest): Mono<UserIdentity>
}