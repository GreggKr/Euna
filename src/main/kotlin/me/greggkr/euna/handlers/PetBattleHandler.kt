package me.greggkr.euna.handlers

import net.dv8tion.jda.core.entities.User
import java.util.*

class PetBattleHandler {
    private val battleRequests = HashMap<Long, Long>()

    fun add(sender: User, target: User) {
        battleRequests[sender.idLong] = target.idLong
    }

    fun hasActive(sender: User, target: User): Boolean {
        return battleRequests.contains(sender.idLong) && battleRequests[sender.idLong] == target.idLong
    }

    fun remove(target: User) {
        battleRequests.remove(battleRequests[target.idLong])
    }
}