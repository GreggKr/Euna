package me.greggkr.euna.commands.econ

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import me.greggkr.euna.util.Emoji
import me.greggkr.euna.util.WeightedRandom
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "fish", triggers = [
    "fish", "f"
], attributes = [
    CommandAttribute(key = "economy")
], description = "Spends $10 to fish.")
class FishCommand : Command {
    override fun execute(message: Message, a: String) {
        val channel = message.channel
        val author = message.author

        val amount = 10
        val current = Euna.data.getMoney(author)

        if (current < amount) {
            channel.sendMessage("${Emoji.X} **You do not have enough money to do that.**").queue()
            return
        }

        val random = WeightedRandom<Pair<Double, String>>()
        random.addAll(Euna.data.getFishChances(message.guild))

////        SIMULATION
//        val goal = 55_000
//        var i = 0
//
//        channel.sendMessage("goal: $goal").queue()
//        while (amount < goal) {
//            if (amount < 0) {
//                channel.sendMessage("failed after $i").queue()
//                return
//            }
//
//            amount -= 10
//            amount += random.get()!!.first
//            println(amount)
//
//            i++
//        }
//
//        channel.sendMessage("success after $i").queue()

        val pair = random.get()

        if (pair == null) {
            channel.sendMessage("${Emoji.X} **Something went wrong.**").queue()
            return
        }

        Euna.data.decreaseMoney(author, amount * 1.0)

        Euna.data.increaseMoney(author, pair.first)

        val sName = pair.second.split(Regex("\\s+"))

        if (!sName.isEmpty() && sName.size >= 2) {
            Euna.data.increaseFish(author, sName[0].toLowerCase(), 1)
        }

        channel.sendMessage("You caught${if (pair.second == "Nothing") "" else (if (isVowel(pair.second[0])) " an" else " a")} ${pair.second}.\n" +
                "You spent $10 and won $${pair.first} for a net gain of $${pair.first - amount}, ${author.asMention}.\n" +
                "${pair.first} - $amount = ${pair.first - amount}; ${random.getChance(pair)}%").queue()
    }

    private fun isVowel(c: Char): Boolean {
        return "AEIOUaeiou".indexOf(c) != -1
    }
}