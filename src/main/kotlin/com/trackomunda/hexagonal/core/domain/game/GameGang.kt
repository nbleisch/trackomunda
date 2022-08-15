package com.trackomunda.hexagonal.core.domain.game

import com.trackomunda.hexagonal.core.domain.Gang
import java.util.*

data class GameGang(
    val id: String = UUID.randomUUID().toString(),
    val gang: Gang,
    val ganger: List<GameFighter> = gang.ganger.map { GameFighter(ganger = it) },
    var isBottleCheckWasRequired: Boolean = false
) {
    val startingCrewSize: Int
        get() {
            return ganger.filter { it.isPartOfCrew }.size
        }

    fun calculateBottleCheckModifier() = ganger.filter { it.isPartOfCrew && (it.isOutOfAction || it.status == FighterStatus.SERIOUSLY_INJURED) }.size

    internal fun noFighterIsReady(): Boolean {
        TODO("Not yet implemented")
    }

    internal fun makeAllFightersReady() {
        TODO("Not yet implemented")
    }

    fun reset() {
        ganger.forEach { ganger ->
            ganger.reset()
        }
    }
}
