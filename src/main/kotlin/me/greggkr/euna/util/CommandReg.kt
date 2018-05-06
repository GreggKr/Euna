package me.greggkr.euna.util

import me.diax.comportment.jdacommand.Command
import me.greggkr.euna.commands.admin.SettingsCommand
import me.greggkr.euna.commands.admin.vote.AddVoteChannelCommand
import me.greggkr.euna.commands.admin.vote.RemoveVoteChannelCommand
import me.greggkr.euna.commands.econ.*
import me.greggkr.euna.commands.extra.AboutCommand
import me.greggkr.euna.commands.extra.HelpCommand
import me.greggkr.euna.commands.extra.StatsCommand
import me.greggkr.euna.commands.moderation.BanCommand
import me.greggkr.euna.commands.moderation.KickCommand
import me.greggkr.euna.commands.moderation.UnbanCommand
import me.greggkr.euna.commands.moderation.WarnCommand
import me.greggkr.euna.commands.owner.EvalCommand
import me.greggkr.euna.commands.pet.PetCommand
import me.greggkr.euna.commands.weeb.img.HugCommand
import me.greggkr.euna.commands.weeb.img.KissCommand
import me.greggkr.euna.commands.weeb.img.NSFWImageCommand

class CommandReg {
    private val commands = HashSet<Command>()

    init {
        register(
                /* Economy */
                CasinoCommand(),
                FishCommand(),
                FishtankCommand(),
                MoneyCommand(),
                TransferCommand(),
                VoteCommand(),

                /* Pet */
                PetCommand(),

                /* Extra */
                AboutCommand(),
                HelpCommand(),
                StatsCommand(),

                /* Weeb */
                /* Image */
                HugCommand(),
                KissCommand(),
                NSFWImageCommand(),
                /* End Weeb */

                /* Moderation */
                KickCommand(),
                BanCommand(),
                UnbanCommand(),
                WarnCommand(),

                /* Admin */
                SettingsCommand(),
                /* Vote */
                AddVoteChannelCommand(),
                RemoveVoteChannelCommand(),
                /* End Admin */

                /* Owner */
                EvalCommand()
        )
    }

    private fun register(vararg command: Command) {
        commands.addAll(command)
    }

    fun getCommands(): Set<Command> = commands
}