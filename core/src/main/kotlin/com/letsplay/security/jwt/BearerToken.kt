package com.letsplay.security.jwt

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils

data class BearerToken(val token: String) : AbstractAuthenticationToken(AuthorityUtils.NO_AUTHORITIES) {
    override fun getCredentials(): Any = token

    override fun getPrincipal(): Any  = token
}
