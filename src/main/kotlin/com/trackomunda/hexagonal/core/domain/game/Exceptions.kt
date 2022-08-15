package com.trackomunda.hexagonal.core.domain

import com.trackomunda.hexagonal.core.domain.game.*


class FighterDoesNotExistException(val fighterId: FighterId) : RuntimeException("Fighter with ID $fighterId does not exist")
class FighterActionNotAvailableException(val action: FighterAction, val ganger: GameFighter) : RuntimeException()
class RequiredFighterStatusDoesNotMatchException(val requiredFighterStatus: List<FighterStatus>, val fighter: GameFighter) :
    RuntimeException("Fighter with status ${fighter.status} does not match one of the following required status: ${requiredFighterStatus.joinToString(",")}")

class RequiredGameStatusDoesNotMatchException(val requiredGameStatus: GameStatus, val game: Game) :
    RuntimeException("Game with status ${game.status.name} does not match the required status: ${requiredGameStatus.name}")
