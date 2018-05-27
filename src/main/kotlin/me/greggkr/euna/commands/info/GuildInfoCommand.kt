package me.greggkr.euna.commands.info

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Message
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@CommandDescription(name = "guildinfo", triggers = [
    "guildinfo", "ginfo", "serverinfo", "sinfo"
], attributes = [
    (CommandAttribute(key = "extra"))
], description = "Displays useful info about a guild")
class GuildInfoCommand : Command {
    override fun execute(message: Message, a: String) {
        val guild = message.guild

        val discordCreated = OffsetDateTime.of(2015, 5, 18, 0, 0, 0, 0, ZoneOffset.UTC)
        val guildCreated = guild.creationTime
        val today = OffsetDateTime.now()
        message.channel.sendMessage(EmbedBuilder()
                .setColor(Euna.data.color)
                .setTitle("Info for ${guild.name}")
                .setThumbnail(guild.iconUrl ?: null)
                .addField("Members", guild.members.filter { !it.user.isBot }.size.toString(), true)
                .addField("Created", "$guildCreated (${guildCreated.until(today, ChronoUnit.DAYS)} days ago)\n" +
                        "${discordCreated.until(guildCreated, ChronoUnit.DAYS)} days after Discord launch", true)
                .addField("Owner", guild.owner.asMention, true)
                .addField("Roles", guild.roles.size.toString(), true)
                .build())
                .queue()
    }
}