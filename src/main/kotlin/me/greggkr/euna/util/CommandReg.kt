package me.greggkr.euna.util

import me.diax.comportment.jdacommand.Command
import me.greggkr.euna.commands.econ.CasinoCommand
import me.greggkr.euna.commands.econ.FishCommand
import me.greggkr.euna.commands.econ.MoneyCommand
import me.greggkr.euna.commands.econ.TransferCommand
import me.greggkr.euna.commands.extra.AboutCommand
import me.greggkr.euna.commands.extra.SettingsCommand
import me.greggkr.euna.commands.owner.EvalCommand

class CommandReg {
    private val commands = HashSet<Command>()

    init {
        register(
                /* Economy */
                CasinoCommand(),
                FishCommand(),
                MoneyCommand(),
                TransferCommand(),

                /* Extra */
                AboutCommand(),
                SettingsCommand(),

                /* Owner */
                EvalCommand()
        )
    }

    private fun register(vararg command: Command) {
        commands.addAll(command)
    }

    fun getCommands(): Set<Command> = commands
}