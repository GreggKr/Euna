package me.greggkr.euna.commands.econ

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import me.greggkr.euna.util.Emoji
import me.greggkr.euna.util.WeightedRandom
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "casino", triggers = [
    "casino"
], description = "Spends a specified amount of money from the user's balance to be gambled with.")
class CasinoCommand : Command {
    override fun execute(message: Message, a: String) {
        val channel = message.channel
        val author = message.author
        if (a.isEmpty()) {
            channel.sendMessage("${Emoji.X} **Correct usage: e!casino <amount>**").queue()
            return
        }

        var amount = a.toDoubleOrNull()

        if (amount == null) {
            channel.sendMessage("${Emoji.X} **Correct usage: e!casino <amount>**").queue()
            return
        }

        amount = Math.abs(amount)

        val current = Euna.data.getMoney(author)
        if (current < amount) {
            channel.sendMessage("${Emoji.X} **You do not have enough money to do that.**").queue()
            return
        }

        val random = WeightedRandom<Double>()
        random.addAll(Euna.data.getCasinoChances(message.guild))

        val multi = random.get()

        if (multi == null) {
            channel.sendMessage("${Emoji.X} **Something went wrong.**").queue()
            return
        }

        Euna.data.decreaseMoney(author, amount * 1.0)
        val money = amount * multi

        Euna.data.increaseMoney(author, money)

        val net = amount * multi - amount


        channel.sendMessage("You spent $$amount and won $$money for a net gain of $$net, ${author.asMention}.\n" +
                "$amount * $multi - $amount = $net; ${random.getChance(multi)}%").queue()
    }
}