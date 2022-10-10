package com.letsplay.security

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("api")
class LoginController(private val loginService: LoginService) {

    @GetMapping("me")
    suspend fun me(@AuthenticationPrincipal principal: Principal): String {
        return principal.name
    }

    @PostMapping("login/basic")
    suspend fun basicLogin(@RequestBody request: BasicLoginRequest): Session {
        return loginService.basicLogin(request)
    }
}