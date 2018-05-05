package me.greggkr.euna.commands.weeb.img

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import me.greggkr.euna.util.Emoji
import me.greggkr.euna.util.ImageType
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "hug", triggers = [
    "hug"
], attributes = [
    (CommandAttribute(key = "anime"))
], description = "Shows an anime hug.")
class HugCommand : Command {
    override fun execute(message: Message, a: String) {
        val channel = message.channel

        val image = Euna.ram.getRandomImage(ImageType.HUG)

        if (image == null) {
            channel.sendMessage("${Emoji.X} Could not get image.").queue()
            return
        }

        channel.sendMessage(EmbedBuilder()
                .setColor(Euna.data.color)
                .setTitle("owo hug image")
                .setImage(Euna.ram.getImageFromPath(image.path))
                .build())
                .queue()
    }
}