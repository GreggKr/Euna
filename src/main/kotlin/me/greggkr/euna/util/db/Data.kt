package me.greggkr.euna.util.db

import com.google.gson.GsonBuilder
import me.greggkr.euna.Euna
import me.greggkr.euna.util.Pet
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.User
import java.awt.Color
import java.util.*
import kotlin.collections.HashMap

class Data(private val db: Database) {
    private val defaultCasinoChances = HashMap<Double, Double>()
    //    HashMap<Pair<Money, Fish>, Chance>
    private val defaultFishChances = HashMap<Pair<Double, String>, Double>()
    private val owners: List<Long> = Arrays.asList(
            184041169796333568L
    )

    private val gson = GsonBuilder().setPrettyPrinting().create()


    val color = Color(200, 66, 244)

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

        defaultFishChances[Pair(0.0, "Nothing")] = 20.0
        defaultFishChances[Pair(10.0, "Common Fish")] = 35.0
        defaultFishChances[Pair(15.0, "Uncommon Fish")] = 30.0
        defaultFishChances[Pair(30.0, "Rare Fish")] = 10.0
        defaultFishChances[Pair(50.0, "Epic Fish")] = 5.0
        defaultFishChances[Pair(100.0, "Megaladon (Mythical)")] = 1.0
        defaultFishChances[Pair(100.0, "The Loch Ness Monster (Mythical)")] = .25
        defaultFishChances[Pair(100.0, "Leviathan (Mythical)")] = .1
    }

    fun isOwner(id: Long): Boolean {
        return owners.contains(id)
    }

    fun isOwner(id: String): Boolean {
        return owners.contains(id.toLong())
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

    fun getFishChances(guild: Guild): Map<Pair<Double, String>, Double> {
        val chances = db.getFishChances(guild.id)
        return chances ?: defaultFishChances
    }

    fun getVotingStreak(user: User): Int {
        return db.getVotingStreak(user.id)
    }

    fun setVotingStreak(user: User, amount: Int) {
        db.setVotingStreak(user.id, amount)
    }

    fun increaseVotingStreak(user: User, amount: Int) {
        val startingStreak = getVotingStreak(user)
        setVotingStreak(user, startingStreak + amount)
    }

    fun getFish(user: User): MutableMap<String, Int> {
        val fish = db.getFish(user.id)
        return fish ?: HashMap()
    }

    fun setFish(user: User, fish: MutableMap<String, Int>) {
        db.setFish(user.id, fish)
    }

    fun getCommonFish(user: User): Int {
        val fish = getFish(user)
        return fish["common"] ?: 0
    }

    fun getUncommonFish(user: User): Int {
        val fish = getFish(user)
        return fish["uncommon"] ?: 0
    }

    fun getRareFish(user: User): Int {
        val fish = getFish(user)
        return fish["rare"] ?: 0
    }

    fun getEpicFish(user: User): Int {
        val fish = getFish(user)
        return fish["epic"] ?: 0
    }

    fun increaseFish(user: User, type: String, amount: Int) {
        val fish = getFish(user)
        val current = fish[type]
        fish[type] = (current ?: 0) + amount
        setFish(user, fish)
    }

    fun getPet(user: User): Pet? {
        val petJson = db.getPet(user.id)
        if (petJson.isNullOrEmpty()) return null

        return gson.fromJson(petJson, Pet::class.java)
    }

    fun setPet(user: User, pet: Pet) {
        val json = gson.toJson(pet)

        db.setPet(user.id, json)
    }
}