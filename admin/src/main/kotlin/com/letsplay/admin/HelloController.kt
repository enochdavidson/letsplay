package com.letsplay.admin

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.security.Principal

@RestController
class HelloController {

    @Value("\${spring.application.name}")
    lateinit var applicationName: String;

    @GetMapping("/")
    fun hello(principal: Principal) : Mono<String> = Mono.just("Hello ${principal.name}, Wellcome to $applicationName");
}