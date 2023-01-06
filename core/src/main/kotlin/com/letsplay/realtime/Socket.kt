package com.letsplay.realtime

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
    fun send(address: String, message: Any)

    /**
     * Get all subscribed channels
     *
     * @return channels
     */
    fun getChannels(): Set<String>
}