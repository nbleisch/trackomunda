package com.trackomunda.hexagonal.core.domain.game

import com.trackomunda.hexagonal.core.domain.FighterAction
import com.trackomunda.hexagonal.core.domain.FighterDoesNotExistException
import com.trackomunda.hexagonal.core.domain.RequiredGameStatusDoesNotMatchException
import com.trackomunda.hexagonal.core.domain.game.GameStatus.*
import java.time.LocalDate
import java.util.*

class Game(
    id: String = UUID.randomUUID().toString(),
    name: String,
    gang1: GameGang? = null,
    gang2: GameGang? = null,
    status: GameStatus = GANG_SELECTION,
    date: LocalDate = LocalDate.now(),
    round: Int = 1,
) {

    var id: String = id
        private set

    var name: String = name
        private set

    var gang1: GameGang? = gang1
        private set

    var gang2: GameGang? = gang2
        private set

    var createdDate: LocalDate = date
        private set

    var round: Int = round
        private set

    var status: GameStatus = status
        private set

    init {
        //reset()?
    }

    fun addOrReplaceGang1(gang: GameGang) {
        requireStatus(GANG_SELECTION)
        this.gang1 = gang
    }

    fun addOrReplaceGang2(gang: GameGang) {
        requireStatus(GANG_SELECTION)
        this.gang2 = gang
    }

    fun reset() {
        listOf(gang1, gang2).forEach {
            it?.reset()
        }
        status = GANG_SELECTION
        round = 1
    }

    fun executeFighterAction(fighterId: FighterId, fighterAction: FighterAction) : Game{

        return this
    }

    fun nextRound() {
        requireStatus(GAME_STARTED)
        listOf(gang1, gang2).forEach { gang ->
            gang?.run {
                check(noFighterIsReady())
                makeAllFightersReady()
            }
        }
        round += 1
    }

    fun startGame() {
        requireStatus(DEPLOYMENT)
        require(GAME_STARTED.conditions.invoke(this))
        status = GAME_STARTED
        TODO("Not yet implemented")
    }


    //Move into GameService
    fun randomSelect() {
        requireStatus(CREW_SELECTION)
        TODO("Not yet implemented")
    }

    fun activateFighter(fighterId: FighterId) {
        requireStatus(GAME_STARTED)
        TODO("Not yet implemented")
    }

    fun selectFighterForCrewSelection(fighterId: FighterId) {
        requireStatus(CREW_SELECTION)
        findFighter(fighterId)?.let {
            it.isPartOfCrew = true
        } ?: throw FighterDoesNotExistException(fighterId)
    }

    fun deselectFighterForCrewSelection(fighterId: FighterId) {
        requireStatus(CREW_SELECTION)
        findFighter(fighterId)?.let {
            it.isPartOfCrew = false
        } ?: throw FighterDoesNotExistException(fighterId)
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

    private fun findFighter(fighterId: FighterId): GameFighter? {
        return listOf(gang1, gang2).firstNotNullOfOrNull { gang -> gang?.ganger?.find { ganger -> ganger.id == fighterId } }
    }

    private fun requireStatus(requiredStatus: GameStatus): Game = this.also {
        if (this.status != requiredStatus || !this.status.conditions.invoke(it)) throw RequiredGameStatusDoesNotMatchException(requiredGameStatus = requiredStatus, game = this)
    }


}