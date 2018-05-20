package me.greggkr.euna.handlers

import me.greggkr.euna.Euna
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

class MuteRejoinHandler : ListenerAdapter() {
    override fun onGuildMemberJoin(e: GuildMemberJoinEvent) {
        val guild = e.guild
        val muted = Euna.data.getMutedUsers(guild) ?: return
        val user = e.user

        if (muted.contains(user)) {
            val roles = guild.getRolesByName("Muted", true)

            if (roles.isEmpty()) return

            guild.controller.addRolesToMember(e.member, roles[0]).queue()
        }
    }
}