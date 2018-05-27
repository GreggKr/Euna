package me.greggkr.euna.util

import com.google.gson.annotations.SerializedName

data class Pet(
        val owner: String,
        val name: String,
        val xp: Int,
        val type: String,
        var health: Int,
        val attack: Int,
        val speed: Int,
        @SerializedName("current_love") val currentLove: Int,
        @SerializedName("max_love") val maxLove: Int
) {
    class Builder {
        private var owner = ""
        private var name = "Unnamed Pet"
        private var xp = 0
        private var type = "No Type"
        private var health = 100
        private var attack = 100
        private var speed = 100
        private var currentLove = 0
        private var maxLove = 100

        fun setOwner(owner: String): Builder {
            this.owner = owner
            return this
        }

        fun setName(name: String): Builder {
            this.name = name
            return this
        }

        fun setXP(xp: Int): Builder {
            this.xp = xp
            return this
        }

        fun setType(type: String): Builder {
            this.type = type
            return this
        }

        fun setHealth(health: Int): Builder {
            this.health = health
            return this
        }

        fun setAttack(attack: Int): Builder {
            this.attack = attack
            return this
        }

        fun setSpeed(speed: Int): Builder {
            this.speed = speed
            return this
        }

        fun setCurrentLove(currentLove: Int): Builder {
            this.currentLove = currentLove
            return this
        }

        fun setMaxLove(maxLove: Int): Builder {
            this.maxLove = maxLove
            return this
        }

        fun build(): Pet? {
            if (owner.isEmpty()) return null

            return Pet(owner, name, xp, type, health, attack, speed, currentLove, maxLove)
        }
    }
}