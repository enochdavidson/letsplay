package com.letsplay.realtime

import reactor.core.publisher.Mono

interface Socket {
    /**
     * Id of the socket
     */
    val id: String

    /**
     * Send a message to the address
     *
     * @param address the address
     * @param message the message
     */
    fun send(address: String, message: Any): Mono<Void>

    /**
     * Get all subscribed channels
     *
     * @return channels
     */
    fun getChannels(): Mono<Set<String>>

    /**
     * Disconnect the client
     */
    fun disconnect(): Mono<Void>
}