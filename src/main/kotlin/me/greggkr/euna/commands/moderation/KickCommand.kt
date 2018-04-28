package me.greggkr.euna.commands.moderation

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "kick", triggers = [
    "kick"
], attributes = [
    (CommandAttribute(key = "requiredRole", value = "mod"))
], description = "Kicks a member.")
class KickCommand : Command {
    override fun execute(message: Message, a: String) {
        val channel = message.channel


    }
}