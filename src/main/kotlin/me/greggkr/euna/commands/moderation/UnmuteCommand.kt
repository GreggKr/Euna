package me.greggkr.euna.commands.moderation

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import me.greggkr.euna.util.Emoji
import me.greggkr.euna.util.modlog.LogEntry
import me.greggkr.euna.util.modlog.ModAction
import me.greggkr.euna.util.modlog.ModActionHandler
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "unmute", triggers = [
    "unmute"
], attributes = [
    (CommandAttribute(key = "modRole"))
], description = "Unmutes a member.")
class UnmuteCommand : Command {
    override fun execute(message: Message, a: String) {
        val guild = message.guild
        val channel = message.channel

        if (a.isEmpty()) {
            channel.sendMessage(String.format("${Emoji.X} Correct Usage: %sunmute <user>", Euna.data.getPrefix(guild))).queue()
            return
        }

        if (message.mentionedMembers.isEmpty()) {
            channel.sendMessage(String.format("${Emoji.X} Correct Usage: %sunmute <user>", Euna.data.getPrefix(guild))).queue()
            return
        }

        val member = message.mentionedMembers[0]

        if (!guild.selfMember.canInteract(member)) {
            channel.sendMessage("${Emoji.X} I cannot interact with that user.").queue()
            return
        }

        val roles = guild.getRolesByName("Muted", true)

        if (roles.isEmpty()) {
            channel.sendMessage("${Emoji.X} That user is not muted.").queue()
            return
        }

        val role = roles[0]

        if (!Euna.data.getMutedUsers(guild).contains(member.user)) {
            channel.sendMessage("${Emoji.X} That user is not muted.").queue()
            return
        }


        guild.controller.removeRolesFromMember(member, role).queue()
        Euna.data.removeMutedUser(guild, member.user)
        channel.sendMessage("${Emoji.WHITE_CHECK_MARK} `${message.author.name + "#" + message.author.discriminator}` unmuted `${member.user.name + "#" + member.user.discriminator}`.").queue()
        ModActionHandler.log(guild, LogEntry(ModAction.UNMUTE, message.author, member.user))
    }
}