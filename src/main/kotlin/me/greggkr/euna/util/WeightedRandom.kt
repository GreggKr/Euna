package me.greggkr.euna.util

class WeightedRandom<K> {
    private val values = HashMap<K, Double>()
    private var sum = 0.0

    fun addAll(map: Map<K, Double>) {
        for (e in map) {
            add(e.key, e.value)
        }
    }

    fun add(value: K, chance: Double) {
        if (values[value] != null) {
            sum -= values[value]!!
        }

        values[value] = chance
        sum += chance
    }

    fun getChance(value: K): Double {
        for (e in values) {
            if (e.key == value) return e.value
        }

        return 0.0
    }

    fun get(): K? {
        val random = Math.random()
        val ratio = 1.0f / sum
        var tmp = 0.0

        println(values)

        for (d in values.keys) {
            tmp += values[d]!!

            if (random / ratio <= tmp) {
                println(d)
                return d
            }
        }

        return null
    }
}