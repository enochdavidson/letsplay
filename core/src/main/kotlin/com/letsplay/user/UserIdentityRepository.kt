package com.letsplay.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserIdentityRepository: JpaRepository<UserIdentity, Long> {
    fun findByProviderAndIdentity(provider: String, identity: String): Optional<UserIdentity>
}