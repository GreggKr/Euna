package me.greggkr.euna.commands.extra

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "casino", triggers = [
    "casino"
], description = "Plays the casino.")
class CasinoCommand : Command {
    override fun execute(message: Message, a: String) {
        val chances = Euna.data.getCasinoChances(message.guild)

        var sum = 0.0
        chances.forEach { sum += it.value }

//        val rand = Random().nextInt(sum)
    }
}