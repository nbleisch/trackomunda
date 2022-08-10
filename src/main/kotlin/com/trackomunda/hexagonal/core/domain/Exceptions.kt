package com.trackomunda.hexagonal.core.domain

class FighterActionNotAvailableException(val action: FighterAction, val ganger: Ganger) : RuntimeException()
class RequiredFighterStatusDoesNotMatchException(val requiredFighterStatus: List<FighterStatus>, val ganger: Ganger) :
    RuntimeException("Ganger with status ${ganger.status} does not match one of the following required status: ${requiredFighterStatus.joinToString(",")}")

class RequiredGameStatusDoesNotMatchException(val requiredGameStatus: GameStatus, val game: Game) :
    RuntimeException("Game with status ${game.status.name} does not match the required status: ${requiredGameStatus.name}")
