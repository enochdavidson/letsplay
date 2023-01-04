package com.letsplay.security

import com.letsplay.security.jwt.JwtException
import com.letsplay.security.jwt.JwtSupport
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class LetsplayAuthenticationManager(private val userDetailsService: ReactiveUserDetailsService,
                                    private val jwtSupport: JwtSupport) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        return mono {
            if (authentication is BearerTokenAuthenticationToken) {
                val username = jwtSupport.getUsername(authentication)
                val user = userDetailsService.findByUsername(username).awaitSingleOrNull()
                if (user != null && jwtSupport.isValid(authentication, user)) {
                    return@mono UsernamePasswordAuthenticationToken(user.username, user.password, user.authorities)
                }
            }
            throw JwtException("Invalid Jwt token")
        }
    }
}

