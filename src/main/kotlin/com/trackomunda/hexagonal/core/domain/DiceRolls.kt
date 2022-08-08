package com.trackomunda.hexagonal.core.domain

interface DiceRoll : Comparable<Int> {
    fun value(): Int
    override fun compareTo(other: Int): Int = value().compareTo(other)
}

data class OneD6(private val diceRoll: Int) : DiceRoll {
    init {
        require(diceRoll in 1..6)
    }
    override fun value() = diceRoll

}

data class TwoD6(private val diceRoll: Int) : DiceRoll {
    init {
        require(diceRoll in 2..12)
    }
    override fun value(): Int = diceRoll
}