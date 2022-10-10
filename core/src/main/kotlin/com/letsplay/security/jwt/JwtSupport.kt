package com.letsplay.security.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class JwtSupport {

    @Value("\${letsplay.security.token.secret}")
    private lateinit var secretKey: String

    @Value("\${letsplay.security.token.expiration-minutes}")
    private val expirationMinutes: Long = 5

    private val jwtParser: JwtParser by lazy {
        Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.toByteArray(Charsets.UTF_8)))
            .build()
    }

    fun generateToken(user: UserDetails): BearerToken {
        val token = Jwts.builder()
            .setSubject(user.username)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plus(expirationMinutes, ChronoUnit.MINUTES)))
            .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray(Charsets.UTF_8)))
            .compact()
        return BearerToken(token)
    }

    fun getUsername(token: BearerToken): String {
        return parseToken(token).body.subject
    }

    fun isValid(token: BearerToken, user: UserDetails): Boolean {
        val jwt = parseToken(token)
        return jwt.body.expiration.after(Date.from(Instant.now()))
                && jwt.body.subject.equals(user.username)
    }

    private fun parseToken(token: BearerToken): Jwt<*, Claims> {
        try {
            return jwtParser.parseClaimsJws(token.token)
        } catch (e: Exception) {
            when(e) {
                is ExpiredJwtException -> throw CredentialsExpiredException("Jwt is expired", e)
                else -> throw JwtException("Unknown Jwt exception", e)
            }
        }
    }
}