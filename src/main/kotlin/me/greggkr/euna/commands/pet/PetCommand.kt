package me.greggkr.euna.commands.pet

import com.jagrosh.jdautilities.commons.utils.FinderUtil
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
            when (first) {
                "test" -> {
                    val pet = Pet.Builder()
                            .setOwner(author.id)
                            .setAttack(55)
                            .build()!!

                    Euna.data.setPet(author, pet)
                }
                "battle" -> when {
                    args[1].toLowerCase() == "accept" -> {
                        val users = FinderUtil.findUsers(a, message.jda)
                        if (users.isEmpty()) {
                            channel.sendMessage("${Emoji.X} User not found.").queue()
                            return
                        }

                        val user = users[0]

                        if (!Euna.battleHandler.hasActive(user, author)) {
                            channel.sendMessage("${Emoji.X} That user has not sent you a request.").queue()
                            return
                        }

                        Euna.battleHandler.remove(user)

                        channel.sendMessage("starting battle between ${author.name} and ${user.name}").queue()

                        // start battle
                    }

                    args[1].toLowerCase() == "deny" -> {
                        val users = FinderUtil.findUsers(a, message.jda)
                        if (users.isEmpty()) {
                            channel.sendMessage("${Emoji.X} User not found.").queue()
                            return
                        }

                        val user = users[0]

                        if (!Euna.battleHandler.hasActive(user, author)) {
                            channel.sendMessage("${Emoji.X} That user has not sent you a request.").queue()
                            return
                        }

                        Euna.battleHandler.remove(user)

                        channel.sendMessage("${user.asMention}, ${author.asMention} has denied your battle request.").queue()
                    }

                    else -> {
                        val petOne = Euna.data.getPet(author)

                        if (petOne == null) {
                            channel.sendMessage("${Emoji.X} You do not have a pet.").queue()
                            return
                        }

                        val users = FinderUtil.findUsers(a, message.jda)
                        if (users.isEmpty()) {
                            channel.sendMessage("${Emoji.X} User not found.").queue()
                            return
                        }

                        val user = users[0]
                        val petTwo = Euna.data.getPet(user)

                        if (petTwo == null) {
                            channel.sendMessage("${Emoji.X} That user does not have a pet.").queue()
                            return
                        }

                        if (petOne.health <= 0 || petTwo.health <= 0) {
                            channel.sendMessage("${Emoji.X} One of your pets does not have enough health to start a battle.").queue()
                            return
                        }

                        channel.sendMessage("${user.asMention}, ${author.asMention} has challenged you to a battle with a bet of $<bet>.\n" +
                                "You can either accept this challenge by entering `e!pet battle accept @${user.name}#${user.discriminator}` " +
                                "or decline the challenge by entering `e!pet battle deny @${author.name}#${author.discriminator}").queue()

                        Euna.battleHandler.add(author, user)
                    }
                }
                "pat" -> {
                    val pet = Euna.data.getPet(author)

                    if (pet == null) {
                        channel.sendMessage("${Emoji.X} You do not have a pet.").queue()
                        return
                    }

                    channel.sendMessage("*pat pat*").queue()
                }
                "cuddle" -> {
                    val pet = Euna.data.getPet(author)

                    if (pet == null) {
                        channel.sendMessage("${Emoji.X} You do not have a pet.").queue()
                        return
                    }

                    channel.sendMessage("*nuzzle nuzzle*").queue()
                }
                "hug" -> {
                    val pet = Euna.data.getPet(author)

                    if (pet == null) {
                        channel.sendMessage("${Emoji.X} You do not have a pet.").queue()
                        return
                    }

                    channel.sendMessage("*hug sound*").queue()
                }
            }
        }
    }
}
