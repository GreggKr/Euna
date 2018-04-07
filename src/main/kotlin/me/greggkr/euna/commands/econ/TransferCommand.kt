package me.greggkr.euna.commands.econ

import com.jagrosh.jdautilities.commons.utils.FinderUtil
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import me.greggkr.euna.util.Emoji
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "transfer", triggers = [
    "transfer", "pay"
], attributes = [
    CommandAttribute(key = "economy")
], description = "Transfers money from one person to another.")
class TransferCommand : Command {
    override fun execute(message: Message, a: String) {
        val channel = message.channel
        val author = message.author
        if (a.isEmpty()) {
            channel.sendMessage("${Emoji.X} Correct usage: e!transfer <user> | <money>").queue()
            return
        }

        val args = a.split(Regex("\\s\\|\\s"))

        if (args.size < 2) {
            channel.sendMessage("${Emoji.X} Correct usage: e!transfer <user> | <money>").queue()
            return
        }

        val users = FinderUtil.findUsers(args[0], message.jda)

        if (users.isEmpty()) {
            channel.sendMessage("${Emoji.X} User not found.")
            return
        }

        val user = users[0]
        val money: Int

        if (user.id == author.id) {
            channel.sendMessage("${Emoji.X} You cannot send money to yourself.").queue()
            return
        }

        try {
            money = Math.abs(Integer.parseInt(args[1]))
        } catch (e: NumberFormatException) {
            channel.sendMessage("${Emoji.X} Correct usage: e!transfer <user> | <money>").queue()
            return
        }

        if (Euna.data.getMoney(author) < money) {
            channel.sendMessage("${Emoji.X} You do not have enough money to do that.").queue()
            return
        }

        Euna.data.decreaseMoney(author, money * 1.0)
        Euna.data.increaseMoney(user, money * 1.0)
        channel.sendMessage("${Emoji.WHITE_CHECK_MARK} You sent $$money to ${user.asMention}.").queue()
    }
}