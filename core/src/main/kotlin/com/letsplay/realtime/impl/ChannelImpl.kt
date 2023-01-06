package com.letsplay.realtime.impl

import com.letsplay.realtime.Channel
import java.util.concurrent.ConcurrentMap

class ChannelImpl(
    override val id: String,
    private val sockets: ConcurrentMap<String, SocketImpl>,
    private val membership: Membership
) : Channel {

    override fun publish(address: String, message: Any) {
        membership.members(id).forEach {
            sockets[it]?.send(address, message)
        }
    }

    override fun send(sender: String, address: String, message: Any) {
        membership.members(id).forEach {
            if (it != sender) {
                sockets[it]?.send(address, message)
            }
        }
    }

    override fun join(socket: String) {
        membership.join(id, socket)
    }

    override fun leave(socket: String) {
        membership.leave(id, socket)
    }

    override fun getSockets(): Set<String> {
        return membership.members(id)
    }

    override fun delete() {
        membership.delete(id)
    }
}