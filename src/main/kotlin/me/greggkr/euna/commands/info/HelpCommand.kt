package me.greggkr.euna.commands.info

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "help", triggers = [
    "help"
], attributes = [
    CommandAttribute(key = "extra")
], description = "Displays helpful information about the bot.")
class HelpCommand : Command {
    override fun execute(message: Message, a: String) {
        val sb = StringBuilder("**Economy**\n")

        val prefix = "e!"

        for (c in Euna.handler.commands.filter { it.hasAttribute("economy") && !it.hasAttribute("noHelp") }) {
            sb.append("$prefix${c.description.name}\n")
        }

//        for (c in Euna.handler.commands.filter { it.hasAttribute("friendly") && !it.hasAttribute("noHelp") }) {
//            sb.append("$prefix${c.description.name}\n")
//        }
//
//        for (c in Euna.handler.commands.filter { it.hasAttribute("pets") && !it.hasAttribute("noHelp") }) {
//            sb.append("$prefix${c.description.name}\n")
//        }

        sb.append("\n**Extra**\n")

        for (c in Euna.handler.commands.filter { it.hasAttribute("extra") && !it.hasAttribute("noHelp") }) {
            sb.append("$prefix${c.description.name}\n")
        }

        sb.append("\nNeed additional help? Join my support server [here](https://discord.gg/W3ckPhb).")

        message.channel.sendMessage(EmbedBuilder()
                .setColor(Euna.data.color)
                .setDescription(sb.toString())
                .build())
                .queue()
    }
}