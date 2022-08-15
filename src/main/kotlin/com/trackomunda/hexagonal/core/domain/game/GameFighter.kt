package com.trackomunda.hexagonal.core.domain.game

import com.trackomunda.hexagonal.core.domain.*
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.Set
import kotlin.collections.contains
import kotlin.collections.filter
import kotlin.collections.toList
import kotlin.collections.toSet
import kotlin.collections.toTypedArray
import kotlin.properties.Delegates

data class FighterId(
    val id: String = UUID.randomUUID().toString()
)


data class GameFighter(
    val id: FighterId = FighterId(),
    val ganger: Ganger,
) : GangerAttributes by ganger {
    var isOutOfAction: Boolean by Delegates.notNull()
    var isReady: Boolean by Delegates.notNull()
    var isPartOfCrew: Boolean by Delegates.notNull()
    var isBlazed: Boolean by Delegates.notNull()
        private set
    var isInsane: Boolean by Delegates.notNull()
        private set
    var currentWounds: Int by Delegates.notNull()
        private set
    var currentToughness: Int by Delegates.notNull()
        private set
    var status: FighterStatus by Delegates.notNull()
    val actionStack: ArrayDeque<FighterAction> = ArrayDeque()

    init {
        reset()
    }

    fun getActionsAvailable(): Set<FighterAction> = FIGHTER_ACTIONS.filter { action ->
        action.requiredStatus.contains(this.status) && action.additionalCondition.invoke(this)
    }.toSet()


    fun hitWithBlaze(diceRoll: OneD6): GameFighter {
        if (diceRoll >= 4) isBlazed = true
        return this
    }

    fun hitWithCurse(diceRoll: TwoD6): GameFighter {
        if (diceRoll < willpower) isInsane = true
        return this
    }

    fun pin(): GameFighter = requireStatus(FighterStatus.STANDING).apply { status = FighterStatus.PRONE }

    fun engage(): GameFighter = requireStatus(FighterStatus.STANDING, FighterStatus.ENGAGED).apply { status = FighterStatus.ENGAGED }

    fun standUp(): GameFighter = requireStatus(FighterStatus.STANDING, FighterStatus.ENGAGED).apply { status = FighterStatus.STANDING }

    fun injure(): GameFighter = this.apply { status = FighterStatus.SERIOUSLY_INJURED }

    fun recover(): GameFighter = requireStatus(FighterStatus.SERIOUSLY_INJURED).apply {
        status = FighterStatus.PRONE
        applyFleshWound()
    }

    fun applyFleshWound(): GameFighter = requireStatusNot(FighterStatus.SERIOUSLY_INJURED).apply {
        currentToughness = Integer.max(0, currentToughness - 1)
        if (currentToughness == 0) goOutOfAction()
    }

    fun goOutOfAction(): GameFighter = this.apply {
        isOutOfAction = true
    }

    fun applyDamage(damage: Int): GameFighter = this.apply {
        currentWounds = Integer.max(0, currentWounds - damage)
        if (currentWounds == 0) {
            injure()
        }
    }

    fun doAction(action: FighterAction) {
        requireActionAvailable(action)
        when (action) {
            is RunForCover -> pin()
            is Fight -> engage()
            is StandUp -> standUp()
            else -> {}
        }
        actionStack.addLast(action)
    }

    fun reset(): GameFighter {
        isOutOfAction = false
        isReady = true
        isPartOfCrew = false
        isBlazed = false
        isInsane = false
        currentWounds = wounds
        currentToughness = toughness
        status = FighterStatus.STANDING
        actionStack.clear()
        return this
    }

    private fun requireActionAvailable(action: FighterAction): GameFighter =
        this.also { if (!getActionsAvailable().contains(action)) throw FighterActionNotAvailableException(action = action, ganger = this) }

    private fun requireStatus(vararg status: FighterStatus): GameFighter = this.also {
        if (!status.contains(this.status)) throw RequiredFighterStatusDoesNotMatchException(requiredFighterStatus = status.toList(), fighter = this)
    }

    private fun requireStatusNot(vararg status: FighterStatus): GameFighter = this.also {
        requireStatus(*FighterStatus.values().toList().filter { !status.contains(it) }.toTypedArray())
    }
}
