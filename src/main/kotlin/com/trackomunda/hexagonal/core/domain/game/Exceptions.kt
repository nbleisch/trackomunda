package com.trackomunda.hexagonal.core.domain

import com.trackomunda.hexagonal.core.domain.game.*


class FighterDoesNotExistException(val gangerId: GangerId) : RuntimeException("Ganger with ID $gangerId does not exist")
class FighterActionNotAvailableException(val action: FighterAction, val ganger: GameGanger) : RuntimeException()
class RequiredFighterStatusDoesNotMatchException(val requiredFighterStatus: List<FighterStatus>, val ganger: GameGanger) :
    RuntimeException("Ganger with status ${ganger.status} does not match one of the following required status: ${requiredFighterStatus.joinToString(",")}")

class RequiredGameStatusDoesNotMatchException(val requiredGameStatus: GameStatus, val game: Game) :
    RuntimeException("Game with status ${game.status.name} does not match the required status: ${requiredGameStatus.name}")
