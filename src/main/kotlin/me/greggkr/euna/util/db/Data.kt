package me.greggkr.euna.util.db

import me.greggkr.euna.Euna
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.User
import java.awt.Color
import java.util.*
import kotlin.collections.HashMap

class Data(private val db: Database) {
    private val defaultCasinoChances = HashMap<Double, Double>()
    private val owners: List<Long> = Arrays.asList(
            184041169796333568L
    )

    init {
        defaultCasinoChances[0.0] = 40.0
        defaultCasinoChances[0.5] = 20.0
        defaultCasinoChances[1.25] = 15.0
        defaultCasinoChances[1.50] = 10.0
        defaultCasinoChances[1.75] = 6.0
        defaultCasinoChances[2.0] = 5.0
        defaultCasinoChances[2.5] = 2.0
        defaultCasinoChances[5.0] = 1.5
        defaultCasinoChances[10.0] = 0.5
    }

    fun getOwner(): String {
        val user = Euna.jda.getUserById(owners[0])
        return if (user == null) "Gregg" else "${user.name}#${user.discriminator}"
    }

    fun getMoney(user: User): Double {
        val rawMoney = db.getMoney(user.id)
        return if (rawMoney == -1.0) 0.0 else rawMoney
    }

    fun setMoney(user: User, amount: Double) {
        db.setMoney(user.id, amount)
    }

    fun increaseMoney(user: User, amount: Double) {
        val startingMoney = getMoney(user)
        setMoney(user, startingMoney + amount)
    }

    fun decreaseMoney(user: User, amount: Double) {
        val startingMoney = getMoney(user)
        setMoney(user, startingMoney - amount)
    }

    fun getCasinoChances(guild: Guild): Map<Double, Double> {
        val chances = db.getCasinoChances(guild.id)
        return chances ?: defaultCasinoChances
    }

    val color = Color(200, 66, 244)

    fun isOwner(id: Long): Boolean {
        return owners.contains(id)
    }

    fun isOwner(id: String): Boolean {
        return owners.contains(id.toLong())
    }
}