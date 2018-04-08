package me.greggkr.euna.util

import me.diax.comportment.jdacommand.Command
import me.greggkr.euna.commands.econ.*
import me.greggkr.euna.commands.extra.AboutCommand
import me.greggkr.euna.commands.extra.HelpCommand
import me.greggkr.euna.commands.extra.SettingsCommand
import me.greggkr.euna.commands.extra.StatsCommand
import me.greggkr.euna.commands.owner.EvalCommand
import me.greggkr.euna.commands.pet.PetCommand

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
                SettingsCommand(),
                StatsCommand(),

                /* Owner */
                EvalCommand()
        )
    }

    private fun register(vararg command: Command) {
        commands.addAll(command)
    }

    fun getCommands(): Set<Command> = commands
}