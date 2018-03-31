package me.greggkr.euna.util

enum class Emoji(private val unicode: String) {
    X("\u274C"),
    WHITE_CHECK_MARK("\u2705");

    override fun toString(): String {
        return unicode
    }
}