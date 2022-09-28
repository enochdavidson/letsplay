package com.letsplay.realtime.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler
import org.springframework.web.util.pattern.PathPatternRouteMatcher

@Configuration
class RealtimeConfig {

    @Bean
    fun rSocketMessageHandler(): RSocketMessageHandler {
        return RSocketMessageHandler().apply {
            rSocketStrategies = rSocketStrategies()
        }
    }

    @Bean
    fun rSocketStrategies(): RSocketStrategies {
        return RSocketStrategies.builder()
            .encoders { it.add(Jackson2JsonEncoder()) }
            .decoders { it.add(Jackson2JsonDecoder()) }
            .routeMatcher(PathPatternRouteMatcher())
            .build()
    }
}