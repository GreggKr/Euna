package me.greggkr.euna.commands.extra

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "about", triggers = [
    "about", "info"
], description = "Displays information about the bot.")
class AboutCommand : Command {
    override fun execute(message: Message, a: String) {
        message.channel.sendMessage(EmbedBuilder()
                .setColor(Euna.data.color)
                .setDescription("Random bot made by `${Euna.data.getOwner()}` for the guild `2017.07.23`.")
                .setFooter("Made with Kotlin, JDA, and some other stuff", null)
                .build())
                .queue()
    }
}