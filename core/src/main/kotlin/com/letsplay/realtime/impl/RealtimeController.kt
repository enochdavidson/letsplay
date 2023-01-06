package com.letsplay.realtime.impl

import com.google.common.cache.CacheBuilder
import com.letsplay.realtime.Channel
import com.letsplay.realtime.Realtime
import com.letsplay.realtime.Socket
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.annotation.ConnectMapping
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import java.time.Duration

@Controller
class RealtimeController: Realtime {

    private val membership = Membership()

    private val sockets = CacheBuilder.newBuilder()
        .expireAfterAccess(Duration.ofMinutes(TTL_MINUTES))
        .initialCapacity(1000)
        .removalListener<String, SocketImpl> { it.value?.dispose() }
        .build<String, SocketImpl>()
        .asMap()

    private val channels = CacheBuilder.newBuilder()
        .expireAfterAccess(Duration.ofMinutes(TTL_MINUTES))
        .initialCapacity(100)
        .build<String, ChannelImpl>()
        .asMap()

    private val connectedHandlers = mutableListOf<(Socket) -> Unit>()
    private val disconnectedHandlers = mutableListOf<(Socket) -> Unit>()

    @ConnectMapping
    fun onSocketConnected(@AuthenticationPrincipal principal: String, rSocket: RSocketRequester) {
        val socket = SocketImpl(principal, rSocket, membership)
        sockets[principal] = socket

        connectedHandlers.forEach { handler ->
            handler.invoke(socket)
        }

        socket.onDisconnect().doFinally {
            sockets.remove(principal)
            disconnectedHandlers.forEach { handler ->
                handler.invoke(socket)
            }
        }.subscribe()
    }

    override fun getSocket(id: String): Socket {
        return sockets[id]!!
    }

    override fun getChannel(id: String): Channel {
        return channels.computeIfAbsent(id) { ChannelImpl(id, sockets, membership) }
    }

    override fun onSocketConnected(handler: (Socket) -> Unit) {
        connectedHandlers.add(handler)
    }

    override fun onSocketDisconnected(handler: (Socket) -> Unit) {
        disconnectedHandlers.add(handler)
    }

    companion object {
        const val TTL_MINUTES = 60L
    }
}