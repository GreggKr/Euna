package me.greggkr.euna.commands.econ

import com.jagrosh.jdautilities.commons.utils.FinderUtil
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "money", triggers = [
    "money", "balance"
], attributes = [
    CommandAttribute(key = "economy")
], description = "Shows how much money someone has.")
class MoneyCommand : Command {
    override fun execute(message: Message, a: String) {
        val channel = message.channel
        val users = FinderUtil.findUsers(a, message.jda)

        if (a.isEmpty() || users.size == 0) {
            channel.sendMessage("${message.author.asMention}, you currently have $${Euna.data.getMoney(message.author)}.").queue()
            return
        }

        val user = users[0]
        channel.sendMessage("${message.author.asMention}, `${user.name}#${user.discriminator}` currently has $${Euna.data.getMoney(user)}.").queue()
    }
}