package me.greggkr.euna.util

import com.natpryce.konfig.PropertyGroup
import com.natpryce.konfig.getValue
import com.natpryce.konfig.intType
import com.natpryce.konfig.stringType

class Config {
    object bot : PropertyGroup() {
        val discordToken by stringType
    }

    object mongo : PropertyGroup() {
        val user by stringType
        val password by stringType
        val authDBName by stringType
        val dbName by stringType
        val host by stringType
        val port by intType
    }
}