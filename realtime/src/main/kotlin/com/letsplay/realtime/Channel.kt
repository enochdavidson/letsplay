package com.letsplay.realtime

/**
 * TODO
 *
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
    fun publish(address: String, message: Any)

    /**
     * Publish message to everyone, except the [sender]
     *
     * @param sender the author of the message
     * @param address the address
     * @param message the message
     */
    fun send(sender: String, address: String, message: Any)

    /**
     * Join [socket] to the channel
     *
     * @param socket the socket to join
     */
    suspend fun join(socket: String)

    /**
     * Leave out [socket] from the channel
     *
     * @param socket the socket to leave out
     */
    suspend fun leave(socket: String)

    /**
     * Get all sockets
     *
     * @return all sockets
     */
    suspend fun getSockets(): Set<String>

    /**
     * Remove all the sockets and delete the channel
     */
    suspend fun delete()
}