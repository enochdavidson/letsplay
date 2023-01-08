package com.letsplay.security

import com.letsplay.security.jwt.JwtSupport
import com.letsplay.user.UserService
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*
import javax.annotation.PostConstruct

@Service
class LoginService(private val userService: UserService,
                   private val passwordEncoder: PasswordEncoder,
                   private val jwtSupport: JwtSupport,
                   private val authProviders: List<AuthenticationProvider>) {

    private val authProviderMap = mutableMapOf<String, AuthenticationProvider>()

    @PostConstruct
    fun initialize() {
        authProviders.forEach {
            authProviderMap[it.providerId] = it
        }
    }

    suspend fun basicLogin(request: LoginRequest): Session {
        val authProvider = authProviderMap[request.provider]
        if (authProvider != null) {
            val identity = authProvider.authenticate(request).awaitSingleOrNull()
            if (identity != null) {
                return Session(
                    UUID.randomUUID().toString(),
                    identity.user!!.id!!.toString(),
                    jwtSupport.generateToken(identity.user!!.id!!.toString()).token
                )
            } else {
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found or credential is incorrect")
            }
        }
        throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Auth provider '${request.provider}' not found")
    }
}