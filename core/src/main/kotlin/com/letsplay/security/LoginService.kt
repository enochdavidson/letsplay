package com.letsplay.security

import com.letsplay.security.jwt.JwtSupport
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class LoginService(private val userDetailsService: ReactiveUserDetailsService,
                   private val passwordEncoder: PasswordEncoder,
                   private val jwtSupport: JwtSupport) {

    suspend fun basicLogin(request: BasicLoginRequest): Session {
        val user = userDetailsService.findByUsername(request.username).awaitSingleOrNull()
        user?.let {
            if (passwordEncoder.matches(request.password, it.password)) {
                return Session(
                    UUID.randomUUID().toString(),
                    it.username,
                    jwtSupport.generateToken(it).token
                )
            }
        }
        throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is incorrect")
    }
}