package me.greggkr.euna.handlers

import me.greggkr.euna.Euna
import me.greggkr.euna.util.Emoji
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

class VoteChannelHandler : ListenerAdapter() {
    override fun onMessageReceived(e: MessageReceivedEvent) {
        val guild = e.guild
        val channel = e.channel

        if (!Euna.data.isVoteChannel(guild, channel as TextChannel)) return

        e.message.addReaction(Emoji.ARROW_DOUBLE_UP.unicode).queue()
        e.message.addReaction(Emoji.ARROW_DOUBLE_DOWN.unicode).queue()
    }
}