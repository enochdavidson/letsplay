package com.letsplay.config

import com.letsplay.security.LetsplayAuthenticationManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity
import org.springframework.security.config.annotation.rsocket.RSocketSecurity
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor
import org.springframework.web.util.pattern.PathPatternRouteMatcher

@EnableRSocketSecurity
@Configuration
class RealtimeConfig {

    @Bean
    fun rSocketMessageHandler(): RSocketMessageHandler {
        return RSocketMessageHandler().apply {
            rSocketStrategies = rSocketStrategies()
            argumentResolverConfigurer.addCustomResolver(AuthenticationPrincipalArgumentResolver())
        }
    }

    private fun rSocketStrategies(): RSocketStrategies {
        return RSocketStrategies.builder()
            .encoders { it.add(Jackson2JsonEncoder()) }
            .decoders { it.add(Jackson2JsonDecoder()) }
            .routeMatcher(PathPatternRouteMatcher())
            .build()
    }

    @Bean
    fun rSocketInterceptor(rSocket: RSocketSecurity,
                           authManager: LetsplayAuthenticationManager): PayloadSocketAcceptorInterceptor {
        rSocket
            .authorizePayload { authorize ->
                authorize
                    .setup().authenticated()
                    .anyRequest().permitAll()
                    .anyExchange().permitAll()
            }
            .jwt {
                it.authenticationManager(authManager)
            }
        return rSocket.build()
    }
}