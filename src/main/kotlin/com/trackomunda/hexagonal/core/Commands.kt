package com.trackomunda.hexagonal.core

import com.trackomunda.hexagonal.core.domain.FighterAction
import com.trackomunda.hexagonal.core.domain.game.FighterId


interface Command


data class ExecuteFighterActionCommand(val gameId: String, val fighterId: FighterId, val fighterAction: FighterAction) : Command