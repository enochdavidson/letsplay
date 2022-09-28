package com.letsplay.realtime

import reactor.core.publisher.Mono

/**
 * Realtime connection registry to get sockets and channels.
 */
interface Realtime {
    /**
     * Get a socket by id
     *
     * @param id socket id
     * @return socket
     */
    fun getSocket(id: String): Mono<Socket>

    /**
     * Get a channel by id.
     * If the channel is not available this will create a new channel.
     *
     * @param id channel id
     * @return channel
     */
    fun getChannel(id: String): Mono<Channel>
}