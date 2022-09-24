package com.letsplay.realtime.impl

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping

@EnableWebFlux
@Configuration
class RealtimeConfig {

    @Bean
    fun handlerMapping(): HandlerMapping {
        val map = mapOf("/realtime" to RealtimeWebSocketHandler())
        val order = -1 // before annotated controllers
        return SimpleUrlHandlerMapping(map, order)
    }
}