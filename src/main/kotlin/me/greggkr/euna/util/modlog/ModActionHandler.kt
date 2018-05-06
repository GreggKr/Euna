package me.greggkr.euna.util.modlog

import me.greggkr.euna.Euna
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.User

class ModActionHandler {
    companion object {
        fun log(guild: Guild, entry: LogEntry) {
            val channel = Euna.data.getModLog(guild) ?: return

            channel.sendMessage(EmbedBuilder()
                    .setColor(Euna.data.color)
                    .addField("Action", entry.action.toString(), true)
                    .setThumbnail(entry.user.effectiveAvatarUrl)
                    .addField("Mod", "${entry.mod.asMention} (${entry.mod.format()})", true)
                    .addField("User", "${entry.user.asMention} (${entry.user.format()})", true)
                    .addField("Reason", entry.reason ?: "None", true)
                    .setTimestamp(entry.timestamp)
                    .build())
                    .queue()
        }

        private fun User.format(): String {
            return "${this.name}#${this.discriminator} (`${this.id}`)"
        }
    }
}