package com.letsplay.realtime

import reactor.core.publisher.Mono

/**
 * Represents a group or a room. Sockets can join and leave the channels.
 */
interface Channel {
    /**
     * Channel id
     */
    val id: String

    /**
     * Publish message to everyone
     *
     * @param address the address
     * @param message the message
     */
    fun publish(address: String, message: Any): Mono<Void>

    /**
     * Publish message to everyone, except the [sender]
     *
     * @param sender the author of the message
     * @param address the address
     * @param message the message
     */
    fun send(sender: String, address: String, message: Any): Mono<Void>

    /**
     * Join [socket] to the channel
     *
     * @param socket the socket to join
     */
    fun join(socket: String): Mono<Void>

    /**
     * Leave out [socket] from the channel
     *
     * @param socket the socket to leave out
     */
    fun leave(socket: String): Mono<Void>

    /**
     * Get all sockets
     *
     * @return all sockets
     */
    suspend fun getSockets(): Mono<Set<String>>

    /**
     * Remove all the sockets and delete the channel
     */
    fun delete(): Mono<Void>
}