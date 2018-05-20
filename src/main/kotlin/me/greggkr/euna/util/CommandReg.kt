package me.greggkr.euna.util

import me.diax.comportment.jdacommand.Command
import me.greggkr.euna.commands.admin.SettingsCommand
import me.greggkr.euna.commands.admin.vote.AddVoteChannelCommand
import me.greggkr.euna.commands.admin.vote.RemoveVoteChannelCommand
import me.greggkr.euna.commands.econ.*
import me.greggkr.euna.commands.info.AboutCommand
import me.greggkr.euna.commands.info.HelpCommand
import me.greggkr.euna.commands.info.StatsCommand
import me.greggkr.euna.commands.moderation.*
import me.greggkr.euna.commands.owner.EvalCommand
import me.greggkr.euna.commands.pet.PetCommand
import me.greggkr.euna.commands.weeb.img.HugCommand
import me.greggkr.euna.commands.weeb.img.KissCommand
import me.greggkr.euna.commands.weeb.img.NSFWImageCommand
import me.greggkr.euna.commands.weeb.img.NomCommand

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
                NomCommand(),
                NSFWImageCommand(),
                /* End Weeb */

                /* Moderation */
                KickCommand(),
                BanCommand(),
                UnbanCommand(),
                WarnCommand(),
                MuteCommand(),
                UnmuteCommand(),

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