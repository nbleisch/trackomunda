package com.trackomunda.hexagonal.core.domain

import com.trackomunda.hexagonal.core.domain.game.FighterStatus
import com.trackomunda.hexagonal.core.domain.game.FighterStatus.*
import com.trackomunda.hexagonal.core.domain.game.GameGanger

sealed class FighterAction(val name: String, val requiredStatus: Set<FighterStatus> = setOf(STANDING), val additionalCondition: (GameGanger) -> Boolean = { !it.isOutOfAction && !it.isBlazed })

object Shoot : FighterAction("Shoot")
object Move : FighterAction("Move")
object Charge : FighterAction("Charge")
object Reload : FighterAction("Reload")
object FightVersatile : FighterAction("Fight Versatile")
object RunForCover : FighterAction("Run for cover")

object AttemptToPutTheFireOut : FighterAction("Attempt to put the fire out", setOf(STANDING, SERIOUSLY_INJURED, PRONE), additionalCondition = { it.isBlazed })

object StandUp : FighterAction("Stand up", setOf(PRONE))
object BlindFire : FighterAction("Blind fire", requiredStatus = setOf(PRONE))

object Fight : FighterAction("Fight", requiredStatus = setOf(ENGAGED))
object Retreat : FighterAction("Retreat", requiredStatus = setOf(ENGAGED))

val FIGHTER_ACTIONS = listOf(Shoot, Move, Charge, Reload, FightVersatile, StandUp, BlindFire, Fight, Retreat, AttemptToPutTheFireOut)