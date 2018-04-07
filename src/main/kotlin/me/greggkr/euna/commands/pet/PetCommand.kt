package me.greggkr.euna.commands.pet

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "pet", triggers = [
    "pet"
], attributes = [
    (CommandAttribute(key = "pet"))
], description = "Displays information about your pet.")
class PetCommand : Command {
    override fun execute(message: Message, a: String) {
        val channel = message.channel
        val author = message.author

        channel.sendMessage("```\n" +
                "${author.name}#${author.discriminator}'s Pet\n\n" +

                "<pet-name>\n\n" +

                "Lv.<pet-level> (<pet-total-xp>/<pet-total-xp-next-level>)\n" +
                "Ti.<pet-tier>\n\n" +

                "Statistics:\n" +
                "Type: <pet-type>\n" +
                "Health | <heath-points>\n" +
                "Attack | <attack-points>\n" +
                "Speed  | <speed-points>\n" +
                "Love   | <current-love>/<max-love>\n" +
                "```").queue()
    }
}
