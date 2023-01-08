package com.letsplay.security

import com.letsplay.security.jwt.JwtException
import com.letsplay.security.jwt.JwtSupport
import com.letsplay.user.UserService
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class LetsplayAuthenticationManager(private val userService: UserService,
                                    private val jwtSupport: JwtSupport) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        return mono {
            if (authentication is BearerTokenAuthenticationToken) {
                val username = jwtSupport.getUsername(authentication)
                val user = userService.findById(username).awaitSingleOrNull()
                if (user != null && jwtSupport.isValid(authentication, username)) {
                    return@mono UsernamePasswordAuthenticationToken(username, "", emptyList())
                }
            }
            throw JwtException("Invalid Jwt token")
        }
    }
}

