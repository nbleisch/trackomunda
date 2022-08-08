package com.trackomunda.hexagonal.core.domain

import com.trackomunda.hexagonal.core.domain.FighterStatus.*

sealed class FighterAction(val name: String, val status: FighterStatus = STANDING)

object Shoot : FighterAction("Shoot")
object Move : FighterAction("Move")
object Charge : FighterAction("Charge")
object Reload : FighterAction("Reload")
object FightVersatile : FighterAction("Fight Versatile")

object StandUp : FighterAction("Stand up", status = PRONE)
object BlindFire : FighterAction("Blind fire", status = PRONE)

object Fight : FighterAction("Fight", status = ENGAGED)
object Retreat : FighterAction("Retreat", status = ENGAGED)

val FIGHTER_ACTIONS = listOf(Shoot, Move, Charge, Reload, FightVersatile, StandUp, BlindFire, Fight, Retreat)