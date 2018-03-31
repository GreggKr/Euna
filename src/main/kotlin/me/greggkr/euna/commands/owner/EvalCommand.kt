package me.greggkr.euna.commands.owner

import groovy.lang.Binding
import groovy.lang.GroovyShell
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import me.greggkr.euna.util.Emoji
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "eval", triggers = [
    "eval"
], attributes = [
    CommandAttribute(key = "botOwner"),
    CommandAttribute(key = "noHelp")
], description = "Allows you to execute an expression.")
class EvalCommand : Command {
    override fun execute(message: Message, a: String) {
        val channel = message.channel
        val shell = createShell(message)

        try {
            val res = shell.evaluate(a)

            if (res == null) {
                channel.sendMessage("${Emoji.WHITE_CHECK_MARK} Executed.").queue()
                return
            }

            channel.sendMessage("${Emoji.WHITE_CHECK_MARK} Executed.\n```groovy\n$res\n```").queue()
        } catch (e: Exception) {
            channel.sendMessage("${Emoji.X} Failed to execute.").queue()
            e.printStackTrace()
        }
    }

    private fun createShell(message: Message): GroovyShell {
        val binding = Binding()
        binding.setVariable("jda", message.jda)
        binding.setVariable("channel", message.channel)
        binding.setVariable("msg", message)
        binding.setVariable("guild", message.guild)
        binding.setVariable("data", Euna.data)

        return GroovyShell(binding)
    }
}