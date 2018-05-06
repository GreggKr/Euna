package me.greggkr.euna.commands.admin

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import me.greggkr.euna.util.Emoji
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageEmbed
import java.lang.StringBuilder

@CommandDescription(name = "settings", triggers = [
    "settings"
], attributes = [
    (CommandAttribute(key = "adminOnly"))
], description = "Changes settings.")
class SettingsCommand : Command {
    override fun execute(message: Message, a: String) {
        val guild = message.guild
        val channel = message.channel

        if (a.isEmpty()) {
            channel.sendMessage(getHelpEmbed(guild)).queue()
            return
        }

        val args = a.split(Regex("\\s+"))

        val first = args[0]
        when {
            first.equals("prefix", true) -> {
                if (args.size < 2) { // 0 -> prefix, 1 -> new prefix
                    channel.sendMessage(String.format("${Emoji.X} Correct usage: %ssettings prefix <prefix>", Euna.data.getPrefix(guild))).queue()
                    return
                }

                val prefix = args[1]

                Euna.data.setPrefix(guild, prefix)
                channel.sendMessage("${Emoji.WHITE_CHECK_MARK} Set prefix to `$prefix`.").queue()
            }

            first.equals("modrole", true) -> {
                if (args.size < 2) { // 0 -> modrole, 1 -> role
                    channel.sendMessage(String.format("${Emoji.X} Correct usage: %sssetings modrole <role>", Euna.data.getPrefix(guild))).queue()
                    return
                }

                val sb = StringBuilder()

                for (i in 1 until args.size) {
                    sb.append(args[i]).append(" ")
                }

                val roles = guild.getRolesByName(sb.toString().trim(), true)


                if (roles.isEmpty()) {
                    channel.sendMessage("${Emoji.X} Role not found.").queue()
                    return
                }

                val role = roles[0]

                Euna.data.setModRole(guild, role)
                channel.sendMessage("${Emoji.WHITE_CHECK_MARK} Set role to `${role.name}`.").queue()
            }

            first.equals("modlog", true) -> {
                if (message.mentionedChannels.isEmpty()) {
                    if (args.size >= 2) { // 0 -> modlog, 1 -> "none"
                        if (args[1].equals("none", true)) {
                            Euna.data.removeModLog(guild)
                            channel.sendMessage("${Emoji.WHITE_CHECK_MARK} Removed mod log.").queue()
                            return
                        }
                    }

                    channel.sendMessage(String.format("${Emoji.X} Correct usage: %sssetings modlog <channel>", Euna.data.getPrefix(guild))).queue()
                    return
                }

                val modLog = message.mentionedChannels[0]

                Euna.data.setModLog(guild, modLog)
                channel.sendMessage("${Emoji.WHITE_CHECK_MARK} Set mod log to ${modLog.asMention}.").queue()
            }

            first.equals("actionLog", true) -> {
                if (message.mentionedChannels.isEmpty()) {
                    if (args.size >= 2) { // 0 -> actionlog, 1 -> "none"
                        if (args[1].equals("none", true)) {
                            Euna.data.removeActionLog(guild)
                            channel.sendMessage("${Emoji.WHITE_CHECK_MARK} Removed action log.").queue()
                            return
                        }
                    }

                    channel.sendMessage(String.format("${Emoji.X} Correct usage: %sssetings actionlog <channel>", Euna.data.getPrefix(guild))).queue()
                    return
                }

                val actionLog = message.mentionedChannels[0]

                Euna.data.setActionLog(guild, actionLog)
                channel.sendMessage("${Emoji.WHITE_CHECK_MARK} Set action log to ${actionLog.asMention}.").queue()
            }

            else -> {
                channel.sendMessage(getHelpEmbed(guild)).queue()
            }
        }
    }

    private fun getHelpEmbed(guild: Guild): MessageEmbed {
        val modRole = Euna.data.getModRole(guild)
        val modLog = Euna.data.getModLog(guild)
        val actionLog = Euna.data.getActionLog(guild)

        return EmbedBuilder()
                .setColor(Euna.data.color)
                .setTitle("Settings for ${guild.name}")
                .addField("Prefix", Euna.data.getPrefix(guild), true)
                .addField("Mod Role", if (modRole == null) "None" else modRole.asMention, true)
                .addField("Mod Log", if (modLog == null) "None" else modLog.asMention, true)
                .addField("Action Log", if (actionLog == null) "None" else actionLog.asMention, true)
                .build()
    }
}