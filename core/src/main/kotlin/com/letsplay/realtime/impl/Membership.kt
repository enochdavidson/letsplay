package com.letsplay.realtime.impl

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimaps

class Membership {
    private val channelMembers = Multimaps.synchronizedSetMultimap(HashMultimap.create<String, String>())
    private val memberChannels = Multimaps.synchronizedSetMultimap(HashMultimap.create<String, String>())

    fun join(channel: String, member: String) {
        channelMembers.put(channel, member)
        memberChannels.put(member, channel)
    }

    fun leave(channel: String, member: String) {
        channelMembers.remove(channel, member)
        memberChannels.remove(member, channel)
    }

    fun delete(channel: String) {
        val members = channelMembers.removeAll(channel)
        members.forEach {
            memberChannels.remove(it, channel)
        }
    }

    fun members(channel: String): Set<String> {
        return channelMembers.get(channel)
    }

    fun subscriptions(member: String): Set<String> {
        return memberChannels.get(member)
    }
}