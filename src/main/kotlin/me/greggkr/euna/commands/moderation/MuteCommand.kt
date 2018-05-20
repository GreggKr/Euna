package me.greggkr.euna.commands.moderation

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import me.greggkr.euna.util.Emoji
import me.greggkr.euna.util.modlog.LogEntry
import me.greggkr.euna.util.modlog.ModAction
import me.greggkr.euna.util.modlog.ModActionHandler
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "mute", triggers = [
    "mute"
], attributes = [
    (CommandAttribute(key = "modRole"))
], description = "Mutes a member.")
class MuteCommand : Command {
    override fun execute(message: Message, a: String) {
        val guild = message.guild
        val channel = message.channel

        if (a.isEmpty()) {
            channel.sendMessage(String.format("${Emoji.X} Correct Usage: %smute <user> | <reason>", Euna.data.getPrefix(guild))).queue()
            return
        }

        val args = a.split(Regex("\\s\\|\\s"))

        if (message.mentionedMembers.isEmpty()) {
            channel.sendMessage(String.format("${Emoji.X} Correct Usage: %smute <user> | <reason>", Euna.data.getPrefix(guild))).queue()
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

        val roles = guild.getRolesByName("Muted", true)

        val role = if (roles.isEmpty()) {
            guild.controller.createRole()
                    .setName("Muted")
                    .setMentionable(false)
                    .setHoisted(false)
                    .complete()
        } else {
            roles[0]
        }

        if (Euna.data.getMutedUsers(guild).contains(member.user)) {
            channel.sendMessage("${Emoji.X} That user is already muted.").queue()
            return
        }

        for (c in guild.textChannels) {
            try {
                c.createPermissionOverride(role)
                        .setDeny(Permission.MESSAGE_WRITE, Permission.VOICE_CONNECT, Permission.MESSAGE_ADD_REACTION)
                        .queue()
            } catch (ignored: IllegalStateException) {
            }
        }

        guild.controller.addRolesToMember(member, role).queue()
        Euna.data.addMutedUser(guild, member.user)
        channel.sendMessage("${Emoji.WHITE_CHECK_MARK} `${message.author.name + "#" + message.author.discriminator}` muted `${member.user.name + "#" + member.user.discriminator}` for `$reason`.").queue()
        ModActionHandler.log(guild, LogEntry(ModAction.MUTE, message.author, member.user, reason))
    }
}