package com.trackomunda.hexagonal.core.domain

import java.time.LocalDate
import java.util.UUID

class Game(
    id: String = UUID.randomUUID().toString(),
    name: String,
    gang: Gang? = null,
    date: LocalDate = LocalDate.now(),
    round: Int = 1
) {

    var id: String = id
        private set

    var name: String = name
        private set

    var gang: Gang? = gang
        private set

    var createdDate: LocalDate = date
        private set

    var round: Int = round
        private set

    fun addGang(gang: Gang): Unit {
        this.gang = gang
    }

    fun reset() {
        gang?.reset()
        round = 1
    }

    fun nextRound() {
        gang?.run {
            check(noFighterIsReady())
            makeAllFightersReady()
        } ?: checkNotNull(gang)
        checkNotNull(gang)
        round += 1
    }

    fun copy() = Game(id = id, name = name, gang = gang, date = createdDate, round = round)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

        if (id != other.id) return false
        if (name != other.name) return false
        if (gang != other.gang) return false
        if (createdDate != other.createdDate) return false
        if (round != other.round) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (gang?.hashCode() ?: 0)
        result = 31 * result + createdDate.hashCode()
        result = 31 * result + round
        return result
    }
}