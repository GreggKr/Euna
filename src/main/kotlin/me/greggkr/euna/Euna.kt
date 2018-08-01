package me.greggkr.euna

import com.natpryce.konfig.ConfigurationProperties
import me.greggkr.euna.handlers.*
import me.greggkr.euna.util.CommandReg
import me.greggkr.euna.util.Config
import me.greggkr.euna.util.Ram
import me.greggkr.euna.util.db.Data
import me.greggkr.euna.util.db.Database
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import java.io.File

class Euna(val devMode: Boolean) {
    companion object {
        lateinit var jda: JDA
        lateinit var config: ConfigurationProperties
        lateinit var data: Data


        val voteHandler = VoteHandler()
        val handler = me.diax.comportment.jdacommand.CommandHandler()
        val battleHandler = PetBattleHandler()
        val ram = Ram()
    }

    fun start() {
        config = ConfigurationProperties.fromFile(File("config.properties${if (devMode) ".dev" else ""}"))

        data = Data(Database(
                config[Config.mongo.user],
                config[Config.mongo.password],
                config[Config.mongo.authDBName],
                config[Config.mongo.dbName],
                config[Config.mongo.host],
                config[Config.mongo.port]
        ))

        handler.registerCommands(CommandReg().getCommands())

        jda = JDABuilder(AccountType.BOT)
                .setToken(config[Config.bot.discordToken])
                .addEventListener(CommandHandler(handler),
                        ActionLogHandler(),
                        VoteChannelHandler(),
                        MuteRejoinHandler())
                .buildBlocking()
    }
}

fun Array<String>.main() {
    Euna(this.isNotEmpty() && this[0].equals("dev", true)).start()
}