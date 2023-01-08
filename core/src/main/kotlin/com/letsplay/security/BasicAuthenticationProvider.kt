package com.letsplay.security

import com.letsplay.user.Role
import com.letsplay.user.User
import com.letsplay.user.UserIdentity
import com.letsplay.user.UserService
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import javax.transaction.Transactional

@Component
class BasicAuthenticationProvider(
    override val providerId: String = "basic",
    private val userService: UserService,
    private val userDetailsService: ReactiveUserDetailsService,
    private val passwordEncoder: PasswordEncoder
) : AuthenticationProvider {

    @Transactional
    override fun authenticate(request: LoginRequest): Mono<UserIdentity> {
        return mono {
            val userIdentity = userService.findIdentity(providerId, request.identity!!).awaitSingleOrNull()
            if (userIdentity == null) {
                return@mono authenticateByUserDetailsService(request)
            } else {
                if (passwordEncoder.matches(request.credential, userIdentity.credential)) {
                    return@mono userIdentity
                }
            }
            return@mono null
        }
    }

    private suspend fun authenticateByUserDetailsService(request: LoginRequest): UserIdentity? {
        val userDetails = userDetailsService.findByUsername(request.identity).awaitSingleOrNull()
        if (userDetails != null && passwordEncoder.matches(request.credential, userDetails.password)) {
            return createUser(request, userDetails)
        }
        return null
    }

    private suspend fun createUser(request: LoginRequest, userDetails: UserDetails): UserIdentity? {
        val userToSave = User().apply {
            id = null
            name = userDetails.username
            role = Role.USER
        }
        val savedUser = userService.createOrUpdate(userToSave).awaitSingleOrNull()

        val identity = UserIdentity().apply {
            id = null
            provider = providerId
            identity = userDetails.username
            credential = passwordEncoder.encode(request.credential)
            user = savedUser
        }
        return userService.createIdentity(identity).awaitSingleOrNull()
    }
}