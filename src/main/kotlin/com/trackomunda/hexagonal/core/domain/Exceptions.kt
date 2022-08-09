package com.trackomunda.hexagonal.core.domain

class FighterActionNotAvailableException(val action: FighterAction, val ganger: Ganger) : RuntimeException()
class RequiredFighterStatusDoesNotExistException(val requiredFighterStatus: List<FighterStatus>, val ganger: Ganger) :
    RuntimeException("Ganger with status ${ganger.status} does not have one of the following status: ${requiredFighterStatus.joinToString(",")}")
