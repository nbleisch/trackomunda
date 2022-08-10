package com.trackomunda.hexagonal.core.domain

import com.trackomunda.hexagonal.core.domain.GameStatus.*
import java.time.LocalDate
import java.util.*

class Game(
    id: String = UUID.randomUUID().toString(),
    name: String,
    gang1: Gang? = null,
    gang2: Gang? = null,
    status: GameStatus = GANG_SELECTION,
    date: LocalDate = LocalDate.now(),
    round: Int = 1,
) {

    var id: String = id
        private set

    var name: String = name
        private set

    var gang1: Gang? = gang1
        private set

    var gang2: Gang? = gang2
        private set

    var createdDate: LocalDate = date
        private set

    var round: Int = round
        private set

    var status: GameStatus = status
        private set

    fun addOrReplaceGang1(gang: Gang) {
        requireStatus(GANG_SELECTION)
        this.gang1 = gang
    }

    fun addOrReplaceGang2(gang: Gang) {
        requireStatus(GANG_SELECTION)
        this.gang2 = gang
    }

    fun reset() {
        gang1?.reset()
        gang2?.reset()
        status = GANG_SELECTION
        round = 1
    }

    fun nextRound() {
        requireStatus(GAME_STARTED)
        gang1?.run {
            check(noFighterIsReady())
            makeAllFightersReady()
        } ?: checkNotNull(gang1)
        checkNotNull(gang1)
        round += 1
    }

    fun startGame() {
        requireStatus(DEPLOYMENT)
        status = GAME_STARTED
        TODO("Not yet implemented")
    }

    fun randomSelect() {
        requireStatus(CREW_SELECTION)
        TODO("Not yet implemented")
    }

    fun customSelect() {
        requireStatus(CREW_SELECTION)
        TODO("Not yet implemented")
    }

    fun activateFighter() {
        requireStatus(GAME_STARTED)
        TODO("Not yet implemented")
    }

    fun copy() = Game(id = id, name = name, gang1 = gang1, gang2 = gang2, status = status, date = createdDate, round = round)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

        if (id != other.id) return false
        if (name != other.name) return false
        if (gang1 != other.gang1) return false
        if (gang2 != other.gang2) return false
        if (createdDate != other.createdDate) return false
        if (round != other.round) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (gang1?.hashCode() ?: 0)
        result = 31 * result + (gang2?.hashCode() ?: 0)
        result = 31 * result + createdDate.hashCode()
        result = 31 * result + round
        result = 31 * result + status.hashCode()
        return result
    }

    private fun requireStatus(requiredStatus: GameStatus): Game = this.also {
        if (this.status != requiredStatus) throw RequiredGameStatusDoesNotMatchException(requiredGameStatus = requiredStatus, game = this)
    }


}