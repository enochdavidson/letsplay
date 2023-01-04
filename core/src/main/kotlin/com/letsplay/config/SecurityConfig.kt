package com.letsplay.config

import com.letsplay.security.LetsplayAuthenticationManager
import com.letsplay.security.jwt.JwtServerAuthenticationConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import reactor.core.publisher.Mono


@EnableWebFluxSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun mySpringSecurityFilterChain(http: ServerHttpSecurity,
                                    authManager: LetsplayAuthenticationManager,
                                    authConverter: JwtServerAuthenticationConverter): SecurityWebFilterChain {

        val authFilter = AuthenticationWebFilter(authManager)
        authFilter.setServerAuthenticationConverter(authConverter)
        authFilter.setAuthenticationFailureHandler(authenticationFailureHandler())

        http.authorizeExchange()
            .pathMatchers(HttpMethod.POST, "/api/login/**").permitAll()
            .anyExchange().authenticated()
            .and()
            .addFilterAt(authFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .cors().disable()

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder();
    }

    private fun authenticationFailureHandler(): ServerAuthenticationFailureHandler {
        return ServerAuthenticationFailureHandler { filterExchange, _ ->
            Mono.fromRunnable {
                filterExchange.exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                filterExchange.exchange.response.headers.set(HttpHeaders.WWW_AUTHENTICATE, "Bearer")
            }
        }
    }
}