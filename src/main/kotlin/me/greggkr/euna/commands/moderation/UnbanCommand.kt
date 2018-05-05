package me.greggkr.euna.commands.moderation

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import me.greggkr.euna.modlog.LogEntry
import me.greggkr.euna.modlog.ModAction
import me.greggkr.euna.modlog.ModLogHandler
import me.greggkr.euna.util.Emoji
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "unban", triggers = [
    "unban"
], attributes = [
    (CommandAttribute(key = "modRole"))
], description = "Unbans a member.")
class UnbanCommand : Command {
    override fun execute(message: Message, a: String) {
        val guild = message.guild
        val channel = message.channel

        if (a.isEmpty()) {
            channel.sendMessage(String.format("${Emoji.X} Correct Usage: %sunban <user>", Euna.data.getPrefix(guild))).queue()
            return
        }

        val banned = guild.banList.complete()

        val ban = banned.firstOrNull {
            val name = it.user.name + "#" + it.user.discriminator
            name.equals(a, true)
        }

        if (ban == null) {
            channel.sendMessage("${Emoji.X} User `$a` is not banned.").queue()
            return
        }

        val user = ban.user

        channel.sendMessage("${Emoji.WHITE_CHECK_MARK} `${message.author.name + "#" + message.author.discriminator}` unbanned `${user.name + "#" + user.discriminator}`.").queue()
        guild.controller.unban(user).queue()
        ModLogHandler.log(guild, LogEntry(ModAction.UNBAN, message.author, user))
    }
}