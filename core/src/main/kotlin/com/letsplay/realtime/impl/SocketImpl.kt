package com.letsplay.realtime.impl

import com.letsplay.realtime.Socket
import org.springframework.messaging.rsocket.RSocketRequester
import reactor.core.publisher.Mono

class SocketImpl(
    override val id: String,
    private val rSocket: RSocketRequester,
    private val membership: Membership
) : Socket {

    override fun send(address: String, message: Any) {
        rSocket.route(address).data(message).send().subscribe()
    }

    override fun getChannels(): Set<String> {
        return membership.subscriptions(id)
    }

    fun dispose() {
        if (!rSocket.isDisposed) {
            rSocket.dispose()
        }
    }

    fun onDisconnect(): Mono<Void> {
        val socket = rSocket.rsocket()
        return socket?.onClose() ?: Mono.empty()
    }
}