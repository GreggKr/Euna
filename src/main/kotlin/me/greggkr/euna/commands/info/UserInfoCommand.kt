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

@CommandDescription(name = "userinfo", triggers = [
    "userinfo", "uinfo"
], attributes = [
    (CommandAttribute(key = "extra"))
], description = "Displays useful info about a user")
class UserInfoCommand : Command {
    override fun execute(message: Message, a: String) {
        val channel = message.channel
        val mentioned = message.mentionedMembers

        var member = message.member
        if (!mentioned.isEmpty()) {
            member = mentioned[0]
        }

        val user = member.user

        val discordCreated = OffsetDateTime.of(2015, 5, 18, 0, 0, 0, 0, ZoneOffset.UTC)
        val accCreated = user.creationTime
        val joined = member.joinDate
        val today = OffsetDateTime.now()

        channel.sendMessage(EmbedBuilder()
                .setColor(Euna.data.color)
                .setTitle("User information for ${user.name}#${user.discriminator}")
                .setThumbnail(user.effectiveAvatarUrl)
                .addField("Nickname", member.nickname ?: "None", true)
                .addField("Bot?", if (user.isBot) "Yes" else "No", true)
                .addField("Created", "$accCreated (${accCreated.until(today, ChronoUnit.DAYS)} days ago)\n" +
                        "${discordCreated.until(accCreated, ChronoUnit.DAYS)} days after Discord launch", true)
                .addField("Joined", "$joined (${joined.until(today, ChronoUnit.DAYS)} days ago)", true)
                .addField("Status", member.onlineStatus.key.toUpperCase(), true)
                .addField("Money", "${Euna.data.getMoney(user)}", true)
                .build())
                .queue()
    }
}