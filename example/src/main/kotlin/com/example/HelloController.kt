package com.example

import com.letsplay.realtime.Realtime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono
import javax.annotation.PostConstruct

@Controller
class HelloController(private val realtime: Realtime) {

    val log: Logger = LoggerFactory.getLogger(this.javaClass);

    @PostConstruct
    fun initialize() {
        realtime.onSocketConnected {
            log.info("Connected: ${it.id}")
        }

        realtime.onSocketDisconnected {
            log.info("Disconnected: ${it.id}")
        }
    }

    @MessageMapping("route.send")
    fun fireAndForget(message: Message): Mono<Void> {
        log.info("Received (Fire & Forget) $message")
        return Mono.empty()
    }

    @MessageMapping("route.request")
    fun requestResponse(message: Message, socket: RSocketRequester, @AuthenticationPrincipal principal: String): Mono<Message> {
        log.info("Received $message")

        realtime.getSocket(principal).send("route.receive", Message("server", "route.receive"))
        realtime.getSocket(principal).send("route.receive-other", Message("server", "route.receive-other"))

        return Mono.just(Message("server", "Hi $principal, Echo: ${message.text}"));
    }
}