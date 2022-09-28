package com.example

import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.annotation.ConnectMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class HelloController {

    val log = LoggerFactory.getLogger(this.javaClass);

    @ConnectMapping
    fun connected(socket: RSocketRequester): Mono<Void> {
        log.info("Connected")
        return Mono.empty();
    }

    @MessageMapping("hello")
    fun hello(@Payload message: String, socket: RSocketRequester): Mono<String> {
        log.info("Received $message")
        return Mono.just("{msg: Echo $message}");
    }
}