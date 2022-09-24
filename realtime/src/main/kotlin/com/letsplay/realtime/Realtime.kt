package com.letsplay.realtime

import reactor.core.publisher.Mono

interface Realtime {
    fun getSocket(id: String): Mono<Socket>

    fun getChannel(id: String): Mono<Channel>

    fun onConnect(listener: ConnectionListener)

    fun onDisconnect(listener: ConnectionListener)

    fun <T> onMessage(address: String, type: Class<T>, listener: MessageListener<T>)
}

inline fun <reified T> Realtime.onMessage(address: String, noinline listener: (Message<in T>) -> Unit) {
    onMessage(address, T::class.java, listener)
}

fun interface ConnectionListener {
    fun accept(socket: Socket)
}

fun interface MessageListener<T> {
    fun accept(message: Message<in T>)
}