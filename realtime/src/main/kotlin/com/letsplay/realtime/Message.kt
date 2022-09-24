package com.letsplay.realtime

data class Message<T>(
    val socket: Socket,
    val body: T,
    private val replyAddress: String? = null
) {
    fun reply(message: Any) {
        if (replyAddress != null) {
            socket.send(replyAddress, message)
        }
    }
}
