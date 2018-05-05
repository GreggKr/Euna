package me.greggkr.euna

import com.natpryce.konfig.ConfigurationProperties
import me.diax.comportment.jdacommand.CommandHandler
import me.greggkr.euna.handlers.CommandListener
import me.greggkr.euna.handlers.PetBattleHandler
import me.greggkr.euna.handlers.VoteHandler
import me.greggkr.euna.util.CommandReg
import me.greggkr.euna.util.Config
import me.greggkr.euna.util.Ram
import me.greggkr.euna.util.db.Data
import me.greggkr.euna.util.db.Database
import net.dv8tion.jda.bot.sharding.DefaultShardManager
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import java.io.File

class Euna {
    companion object {
        lateinit var jda: JDA
        lateinit var config: ConfigurationProperties
        lateinit var handler: CommandHandler
        lateinit var data: Data

        val voteHandler = VoteHandler()
        val battleHandler = PetBattleHandler()
        val ram = Ram()
    }

    fun start() {
        config = ConfigurationProperties.fromFile(File("config.properties"))
        data = Data(Database(
                config[Config.mongo.user],
                config[Config.mongo.password],
                config[Config.mongo.authDBName],
                config[Config.mongo.dbName],
                config[Config.mongo.host],
                config[Config.mongo.port]
        ))

        handler = CommandHandler()
        handler.registerCommands(CommandReg().getCommands())

        jda = JDABuilder(AccountType.BOT)
                .setToken(config[Config.bot.discordToken])
                .addEventListener(CommandListener(handler))
                .buildBlocking()
    }
}

fun Array<String>.main() {
    Euna().start()
}