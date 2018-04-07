package me.greggkr.euna.commands.pet

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import me.greggkr.euna.util.Emoji
import me.greggkr.euna.util.Pet
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

        val args = a.split(Regex("\\s+"))

        if (args.isEmpty() || args[0].isEmpty()) {
            val pet = Euna.data.getPet(author)

            if (pet == null) {
                channel.sendMessage("${Emoji.X} You do not have a pet.").queue()
                return
            }

            channel.sendMessage("```\n" +
                    "${author.name}#${author.discriminator}'s Pet\n\n" +

                    "${pet.name}\n\n" +

                    "Lv.<pet-level> (${pet.xp}/<pet-total-xp-next-level>)\n" +
                    "Ti.<pet-tier>\n\n" +

                    "Statistics:\n" +
                    "Type: ${pet.type}\n" +
                    "Health | ${pet.health}\n" +
                    "Attack | ${pet.attack}\n" +
                    "Speed  | ${pet.speed}\n" +
                    "Love   | ${pet.currentLove}/${pet.maxLove}\n" +
                    "```").queue()
        } else {
            val first = args[0].toLowerCase()
            if (first == "test") {
                val pet = Pet.Builder()
                        .setOwner(author.id)
                        .setAttack(55)
                        .build()!!

                Euna.data.setPet(author, pet)
            }
        }
    }
}
