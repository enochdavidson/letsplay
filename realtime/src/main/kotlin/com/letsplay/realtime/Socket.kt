package com.letsplay.realtime

interface Socket {
    /**
     * Id of the socket
     */
    val id: String

    /**
     * Send message to the address
     *
     * @param address the address
     * @param message the message
     */
    fun send(address: String, message: Any)

    suspend fun getChannels(): Set<String>

    /**
     * Disconnect the client
     */
    fun disconnect()
}