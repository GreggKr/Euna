package me.greggkr.euna.commands.extra

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "stats", triggers = [
    "stats"
], attributes = [
    CommandAttribute(key = "extra")
], description = "Displays useful statistics about Euna")
class StatsCommand : Command {
    override fun execute(message: Message, a: String) {
        val channel = message.channel
        val jda = message.jda
        
        var users = 0

        jda.guilds.forEach { users += it.members.size }
        channel.sendMessage(EmbedBuilder()
                .setColor(Euna.data.color)
                .setThumbnail(jda.selfUser.effectiveAvatarUrl)
                .addField("Guild Count", "${jda.guilds.size}", true)
                .addField("Members", "$users", true)
                .build())
                .queue()

    }

}