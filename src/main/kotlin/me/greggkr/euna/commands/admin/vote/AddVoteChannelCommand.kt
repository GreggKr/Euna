package me.greggkr.euna.commands.admin.vote

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import me.greggkr.euna.util.Emoji
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "addvotechannel", triggers = [
    "addvotechannel", "addvote"
], attributes = [
    (CommandAttribute(key = "adminOnly"))
], description = "Adds a voting/suggestion channel.")
class AddVoteChannelCommand : Command {
    override fun execute(message: Message, a: String) {
        val guild = message.guild
        val channel = message.channel
        val mentioned = message.mentionedChannels

        if (mentioned.isEmpty()) {
            channel.sendMessage("${Emoji.X} Correct Usage: %saddvote <channel>").queue()
            return
        }

        val ch = mentioned[0]

        if (Euna.data.isVoteChannel(guild, ch)) {
            channel.sendMessage("${Emoji.X} That channel is already a voting channel.").queue()
            return
        }

        Euna.data.addVoteChannel(guild, ch)
        channel.sendMessage("${Emoji.WHITE_CHECK_MARK} Added `${ch.name}` to vote channels.").queue()
    }
}