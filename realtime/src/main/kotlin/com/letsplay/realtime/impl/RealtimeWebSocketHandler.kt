package com.letsplay.realtime.impl

import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class RealtimeWebSocketHandler : WebSocketHandler {
    override fun handle(session: WebSocketSession): Mono<Void> {
        val output = session.receive()
            .map { it.payloadAsText }
            .log()
            .map { session.textMessage("Echo $it") }
        return session.send(output)
    }
}