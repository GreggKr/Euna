package me.greggkr.euna.modlog

import me.greggkr.euna.Euna
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.User

class ModLogHandler {
    companion object {
        fun log(guild: Guild, entry: LogEntry) {
            val channel = Euna.data.getModLog(guild) ?: return

            channel.sendMessage(EmbedBuilder()
                    .setColor(Euna.data.color)
                    .addField("Action", entry.action.toString(), true)
                    .setThumbnail(entry.user.effectiveAvatarUrl)
                    .addField("Mod", "${entry.mod.asMention} (${formatUser(entry.mod)})", true)
                    .addField("User", "${entry.user.asMention} (${formatUser(entry.user)})", true)
                    .addField("Reason", entry.reason ?: "None", true)
                    .setTimestamp(entry.timestamp)
                    .build())
                    .queue()
        }

        private fun formatUser(user: User): String {
            return "${user.name}#${user.discriminator} (`${user.id}`)"
        }
    }
}