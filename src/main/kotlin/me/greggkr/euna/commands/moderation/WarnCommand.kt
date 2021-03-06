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

@CommandDescription(name = "warn", triggers = [
    "warn"
], attributes = [
    (CommandAttribute(key = "modRole"))
], description = "Warns a member.")
class WarnCommand : Command {
    override fun execute(message: Message, a: String) {
        val guild = message.guild
        val channel = message.channel

        if (a.isEmpty()) {
            channel.sendMessage(String.format("${Emoji.X} Correct Usage: %swarn <user> | <reason>", Euna.data.getPrefix(guild))).queue()
            return
        }

        val args = a.split(Regex("\\s\\|\\s"))

        if (message.mentionedMembers.isEmpty()) {
            channel.sendMessage(String.format("${Emoji.X} Correct Usage: %swarn <user> | <reason>", Euna.data.getPrefix(guild))).queue()
            return
        }

        var reason = "No reason provided"
        if (args.size > 1) {
            reason = args[1]
        }

        val member = message.mentionedMembers[0]

        channel.sendMessage("${Emoji.WHITE_CHECK_MARK} `${message.author.name + "#" + message.author.discriminator}` warned `${member.user.name + "#" + member.user.discriminator}` for `$reason`.").queue()
        ModActionHandler.log(guild, LogEntry(ModAction.WARN, message.author, member.user, reason))
    }
}