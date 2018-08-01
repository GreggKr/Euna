package me.greggkr.euna.handlers

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandHandler
import me.greggkr.euna.Euna
import me.greggkr.euna.util.Emoji
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

class CommandHandler(private val handler: CommandHandler) : ListenerAdapter() {
    override fun onMessageReceived(e: MessageReceivedEvent) {
        if (e.author.isBot) return
        if (e.channelType != ChannelType.TEXT) return

        val guild = e.guild
        val prefix = Euna.data.getPrefix(guild)
        val message = e.message.contentRaw
        val channel = e.channel

        if (message == e.guild.selfMember.asMention) {
            channel.sendMessage("Hello ${e.author.asMention}, my name is Euna and my prefix here is **$prefix**. If you need any additional help, you can use ${prefix}help.").queue()
            return
        }

        val member = e.member

        if (!message.startsWith(prefix)) return

        val args: List<String> = message.split(Regex("\\s+"), 2)
        val trigger = args[0].substring(prefix.length)

        val command: Command = handler.findCommand(trigger.toLowerCase()) ?: return

        if (command.hasAttribute("botOwner")) {
            if (!Euna.data.isOwner(e.author.idLong)) return
        }

        if (command.hasAttribute("adminOnly")) {
            if (!member.hasPermission(Permission.MANAGE_SERVER) && !member.hasPermission(Permission.ADMINISTRATOR)) {
                channel.sendMessage("${Emoji.X} You must have the MANAGE_SERVER or ADMINISTRATOR permission to do this.")
                return
            }
        }

        if (command.hasAttribute("modRole")) {
            val modRole = Euna.data.getModRole(guild)

            if (modRole == null) {
                channel.sendMessage("${Emoji.X} Mod role is invalid.").queue()
                return
            }

            if (!member.roles.contains(modRole)) {
                channel.sendMessage("${Emoji.X} You have to have the role `${modRole.name}`.").queue()
                return
            }
        }

        if (command.hasAttribute("nsfw")) {
            if (!(channel as TextChannel).isNSFW) {
                channel.sendMessage("${Emoji.X} This command must be used in an NSFW channel.").queue()
                return
            }
        }

        handler.execute(command, e.message, if (args.size > 1) args[1] else "")

    }
}