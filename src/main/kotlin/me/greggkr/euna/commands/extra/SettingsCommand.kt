package me.greggkr.euna.commands.extra

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "settings", triggers = [
    "settings"
], description = "Change various settings about the bot.")
class SettingsCommand : Command {
    override fun execute(message: Message, a: String) {

    }
}