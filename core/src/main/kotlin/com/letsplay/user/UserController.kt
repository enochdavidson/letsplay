package com.letsplay.user

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class UserController(private val userService: UserService) {

    @MessageMapping("users/me")
    fun getMe(@AuthenticationPrincipal userId: String): Mono<User> {
        return userService.findById(userId)
    }
}