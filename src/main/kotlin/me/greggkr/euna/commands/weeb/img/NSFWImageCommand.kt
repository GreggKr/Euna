package me.greggkr.euna.commands.weeb.img

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import me.greggkr.euna.util.Emoji
import me.greggkr.euna.util.ImageType
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "nsfwimage", triggers = [
    "nsfwimage", "nsfwweeb"
], attributes = [
    (CommandAttribute(key = "anime")),
    (CommandAttribute(key = "nsfw"))
], description = "Shows a nsfw anime image.")
class NSFWImageCommand : Command {
    override fun execute(message: Message, a: String) {
        val channel = message.channel

        val image = Euna.ram.getRandomImage(ImageType.NSFW_GTN)

        if (image == null) {
            channel.sendMessage("${Emoji.X} Could not get image.").queue()
            return
        }

        channel.sendMessage(EmbedBuilder()
                .setColor(Euna.data.color)
                .setTitle("dont tell my mom")
                .setImage(Euna.ram.getImageFromPath(image.path))
                .build())
                .queue()
    }
}