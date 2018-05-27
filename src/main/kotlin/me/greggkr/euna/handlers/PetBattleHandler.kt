package me.greggkr.euna.handlers

import me.greggkr.euna.util.Pet
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.User
import java.util.*

class PetBattleHandler {
    private val battleRequests = HashMap<Long, Long>()

    fun battle(channel: MessageChannel, pet1: Pet, pet2: Pet) {
        // battle sequence
        // TODO: speed + randomness
        var turn: Int = 1
        while (pet1.health > 0 && pet2.health > 0) {
            pet2.health -= pet1.attack
            channel.sendMessage("[T$turn] ${pet1.name} attacked ${pet2.name} for ${pet1.attack} attack. ${pet2.name}'s health is now: ${pet2.health}hp").queue()
            if (pet2.health < 0) {
                channel.sendMessage("[Battle] ${pet2.name} beat ${pet1.name}! Congrats ${channel.jda.getUserById(pet2.owner).asMention}").queue()
                return
            }

            sleep(1250)

            pet1.health -= pet2.attack
            channel.sendMessage("[T$turn] ${pet2.name} attacked ${pet1.name} for ${pet2.attack} attack. ${pet1.name}'s health is now: ${pet1.health}hp").queue()
            if (pet1.health < 0) {
                channel.sendMessage("[Battle] ${pet1.name} beat ${pet2.name}! Congrats ${channel.jda.getUserById(pet1.owner).asMention}").queue()
                return
            }
            sleep(1250)
            turn++
        }

        // Should never be reached
        if (pet1.health > pet2.health) {
            channel.sendMessage("[Battle] ${pet1.name} beat ${pet2.name}! Congrats ${channel.jda.getUserById(pet1.owner).asMention}").queue()
        } else {
            channel.sendMessage("[Battle] ${pet2.name} beat ${pet1.name}! Congrats ${channel.jda.getUserById(pet2.owner).asMention}").queue()
        }

    }

    fun add(sender: User, target: User) {
        battleRequests[sender.idLong] = target.idLong
    }

    fun hasActive(sender: User, target: User): Boolean {
        return battleRequests.contains(sender.idLong) && battleRequests[sender.idLong] == target.idLong
    }

    fun remove(target: User) {
        battleRequests.remove(battleRequests[target.idLong])
    }

    private fun sleep(ms: Long) {
        try {
            Thread.sleep(ms)
        } catch (ignored: InterruptedException) {
        }
    }
}