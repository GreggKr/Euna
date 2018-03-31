package me.greggkr.euna.util

class WeightedRandom {
    private val values = HashMap<Double, Double>()
    private var sum = 0.0

    fun addAll(map: Map<Double, Double>) {
        for (e in map) {
            add(e.key, e.value)
        }
    }

    fun add(value: Double, chance: Double) {
        if (values[value] != null) {
            sum -= values[value]!!
        }

        values[value] = chance
        sum += chance
    }

    fun get(): Double {
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

        println("no")
        return 0.0
    }
}