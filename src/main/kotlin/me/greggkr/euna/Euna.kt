package me.greggkr.euna

import com.natpryce.konfig.ConfigurationProperties
import me.diax.comportment.jdacommand.CommandHandler
import me.greggkr.euna.handlers.CommandListener
import me.greggkr.euna.handlers.PetBattleHandler
import me.greggkr.euna.handlers.VoteHandler
import me.greggkr.euna.util.CommandReg
import me.greggkr.euna.util.Config
import me.greggkr.euna.util.db.Data
import me.greggkr.euna.util.db.Database
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

    /* TEST */
//    val random = WeightedRoh shandom()
//
//    random.add(0.0, 40.0)
//    random.add(0.5, 20.0)
//    random.add(1.25, 15.0)
//    random.add(1.50, 10.0)
//    random.add(1.75, 6.0)
//    random.add(2.0, 5.0)
//    random.add(2.5, 2.0)
//    random.add(5.0, 1.5)
//    random.add(10.0, 0.5)
//
//    val testSize = 1_000_000
//
//    val testMap = HashMap<Double, Double>()
//
//    for (i in 1..testSize) {
//        val r = random.getRandom()
//        testMap[r] = if (testMap[r] == null) 1.0 / testSize else testMap[r]!! + 1.0 / testSize
//    }
//
//    println(testMap)
}