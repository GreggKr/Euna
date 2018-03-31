package me.greggkr.euna.listeners

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandHandler
import me.greggkr.euna.Euna
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

class CommandListener(private val handler: CommandHandler) : ListenerAdapter() {
    override fun onMessageReceived(e: MessageReceivedEvent) {
        if (e.author.isBot) return
        if (e.channelType != ChannelType.TEXT) return

        val prefix = "e!"
        val message = e.message.contentRaw

        if (message == e.guild.selfMember.asMention) {
            e.channel.sendMessage("**Prefix: `$prefix`.**").queue()
            return
        }

        if (!message.startsWith(prefix)) return

        val args: List<String> = message.split(Regex("\\s+"), 2)
        val trigger = args[0].substring(prefix.length)

        val command: Command = handler.findCommand(trigger.toLowerCase()) ?: return

        if (command.hasAttribute("botOwner")) {
            if (!Euna.data.isOwner(e.author.idLong)) return
        }

        handler.execute(command, e.message, if (args.size > 1) args[1] else "")

    }
}