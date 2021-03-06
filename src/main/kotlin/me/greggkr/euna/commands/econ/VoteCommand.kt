package me.greggkr.euna.commands.econ

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import me.greggkr.euna.util.Emoji
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "vote", triggers = [
    "vote", "v"
], attributes = [
    CommandAttribute(key = "economy")
], description = "Allows you to earn some money daily.")
class VoteCommand : Command {
    override fun execute(message: Message, a: String) {
        val channel = message.channel
        val author = message.author

        val streak = Euna.data.getVotingStreak(author)

        if (!a.isEmpty() && a.split(Regex("\\s+"))[0].toLowerCase() == "streak") {
            channel.sendMessage("${author.asMention}, you have voted for me $streak days in a row. As thanks for your continued support, you receive a bonus of $${streak * 1.15} on your vote.").queue()
            return
        }

        if (Euna.voteHandler.isIn(author)) {
            channel.sendMessage("${Emoji.X} You have already voted today.").queue()
            return
        }


        val money = (streak * 1.15) + 10.0

        Euna.voteHandler.add(author)
        Euna.data.increaseMoney(author, money)

        channel.sendMessage("You got $$money for voting. You can vote again in 24 hours.").queue()
    }
}