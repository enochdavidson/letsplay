package com.letsplay.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/users")
class UserController(private val repository: UserRepository) {

    @GetMapping("/")
    suspend fun getAllUsers(): List<User> {
        return repository.findAll()
    }
}