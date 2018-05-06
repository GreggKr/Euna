package me.greggkr.euna.util.modlog

import net.dv8tion.jda.core.entities.User
import java.time.Instant

class LogEntry(val action: ModAction, val mod: User, val user: User, val reason: String? = "None", val timestamp: Instant = Instant.now())