package me.greggkr.euna.handlers

import me.greggkr.euna.Euna
import net.dv8tion.jda.core.entities.User
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class VoteHandler {
    private val recentVotes = ArrayList<User>()
    private val executor = Executors.newSingleThreadScheduledExecutor()

    fun add(user: User) {
        recentVotes.add(user)

        Euna.data.increaseVotingStreak(user, 1)

        executor.schedule({
            recentVotes.remove(user)
        }, 1, TimeUnit.DAYS)
    }

    fun isIn(user: User): Boolean = recentVotes.contains(user)
}