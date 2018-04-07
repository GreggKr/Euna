package me.greggkr.euna.commands.econ

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "fishtank", triggers = [
    "fishtank", "tank"
], attributes = [
    CommandAttribute(key = "economy")
], description = "Displays information about your collected fish.")
class FishtankCommand : Command {
    override fun execute(message: Message, a: String) {
        val channel = message.channel
        val author = message.author

        val common = Euna.data.getCommonFish(author)
        val uncommon = Euna.data.getUncommonFish(author)
        val rare = Euna.data.getRareFish(author)
        val epic = Euna.data.getEpicFish(author)

        channel.sendMessage("```" +
                "${author.name}#${author.discriminator}'s Fishtank\n" +
                "<fishing-points> Points\n\n" +

                "Usual\n" +
                "Common Fish           | * $common\n" +
                "Uncommon Fish         | * $uncommon\n" +
                "Rare Fish             | * $rare\n" +
                "Epic Fish             | * $epic\n" +
                "```").queue()
    }
}