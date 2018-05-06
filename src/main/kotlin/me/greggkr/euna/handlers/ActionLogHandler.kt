package me.greggkr.euna.handlers

import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.hooks.ListenerAdapter
import net.dv8tion.jda.core.requests.restaction.MessageAction
import java.time.Instant
import java.time.format.DateTimeFormatter

class ActionLogHandler : ListenerAdapter() {
    private val dtFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

    private fun escapeFormatting(string: String): String {
        return string
                .replace("*", "\\*")
                .replace("`", "\\`")
                .replace("_", "\\_")
    }

    private fun MessageChannel.sendActionLog(msg: String): MessageAction {
        return this.sendMessage("[${dtFormatter.format(Instant.now())}] $msg")
    }


    private fun User.format(): String {
        return "${this.name}#${this.discriminator} (`${this.id}`)"
    }
}