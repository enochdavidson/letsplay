package com.letsplay.user

import com.letsplay.comon.util.executeBlocking
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val identityRepository: UserIdentityRepository
) {

    fun createOrUpdate(user: User): Mono<User> {
        return executeBlocking { userRepository.save(user) }
    }

    fun existsById(id: String): Mono<Boolean> {
        return executeBlocking { userRepository.existsById(UUID.fromString(id)) }
    }

    fun findById(id: String): Mono<User> {
        return executeBlocking { userRepository.findById(UUID.fromString(id)).orElse(null) }
    }

    fun createIdentity(identity: UserIdentity): Mono<UserIdentity> {
        return executeBlocking { identityRepository.save(identity) }
    }

    fun findIdentity(provider: String, identity: String): Mono<UserIdentity> {
        return executeBlocking { identityRepository.findByProviderAndIdentity(provider, identity).orElse(null) }
    }
}