package me.greggkr.euna.commands.moderation

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import me.greggkr.euna.util.modlog.ModActionHandler
import me.greggkr.euna.util.Emoji
import me.greggkr.euna.util.modlog.LogEntry
import me.greggkr.euna.util.modlog.ModAction
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "ban", triggers = [
    "ban"
], attributes = [
    (CommandAttribute(key = "modRole"))
], description = "Bans a member.")
class BanCommand : Command {
    override fun execute(message: Message, a: String) {
        val guild = message.guild
        val channel = message.channel

        if (a.isEmpty()) {
            channel.sendMessage(String.format("${Emoji.X} Correct Usage: %sban <user> | <reason>", Euna.data.getPrefix(guild))).queue()
            return
        }

        val args = a.split(Regex("\\s\\|\\s"))

        if (message.mentionedMembers.isEmpty()) {
            channel.sendMessage(String.format("${Emoji.X} Correct Usage: %sban <user> | <reason>", Euna.data.getPrefix(guild))).queue()
            return
        }

        var reason = "No reason provided"
        if (args.size > 1) {
            reason = args[1]
        }

        val member = message.mentionedMembers[0]

        if (!guild.selfMember.canInteract(member)) {
            channel.sendMessage("${Emoji.X} I cannot interact with that user.").queue()
            return
        }

        message.guild.controller.ban(member, 0, reason).queue()
        channel.sendMessage("${Emoji.WHITE_CHECK_MARK} `${message.author.name + "#" + message.author.discriminator}` banned `${member.user.name + "#" + member.user.discriminator}` for `$reason`.").queue()
        ModActionHandler.log(guild, LogEntry(ModAction.BAN, message.author, member.user, reason))
    }
}