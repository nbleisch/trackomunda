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

data class GangerId(
    val id: String = UUID.randomUUID().toString()
)


data class GameGanger(
    val id: GangerId = GangerId(),
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


    fun hitWithBlaze(diceRoll: OneD6): GameGanger {
        if (diceRoll >= 4) isBlazed = true
        return this
    }

    fun hitWithCurse(diceRoll: TwoD6): GameGanger {
        if (diceRoll < willpower) isInsane = true
        return this
    }

    fun pin(): GameGanger = requireStatus(FighterStatus.STANDING).apply { status = FighterStatus.PRONE }

    fun engage(): GameGanger = requireStatus(FighterStatus.STANDING, FighterStatus.ENGAGED).apply { status = FighterStatus.ENGAGED }

    fun standUp(): GameGanger = requireStatus(FighterStatus.STANDING, FighterStatus.ENGAGED).apply { status = FighterStatus.STANDING }

    fun injure(): GameGanger = this.apply { status = FighterStatus.SERIOUSLY_INJURED }

    fun recover(): GameGanger = requireStatus(FighterStatus.SERIOUSLY_INJURED).apply {
        status = FighterStatus.PRONE
        applyFleshWound()
    }

    fun applyFleshWound(): GameGanger = requireStatusNot(FighterStatus.SERIOUSLY_INJURED).apply {
        currentToughness = Integer.max(0, currentToughness - 1)
        if (currentToughness == 0) goOutOfAction()
    }

    fun goOutOfAction(): GameGanger = this.apply {
        isOutOfAction = true
    }

    fun applyDamage(damage: Int): GameGanger = this.apply {
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

    fun reset(): GameGanger {
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

    private fun requireActionAvailable(action: FighterAction): GameGanger =
        this.also { if (!getActionsAvailable().contains(action)) throw FighterActionNotAvailableException(action = action, ganger = this) }

    private fun requireStatus(vararg status: FighterStatus): GameGanger = this.also {
        if (!status.contains(this.status)) throw RequiredFighterStatusDoesNotMatchException(requiredFighterStatus = status.toList(), ganger = this)
    }

    private fun requireStatusNot(vararg status: FighterStatus): GameGanger = this.also {
        requireStatus(*FighterStatus.values().toList().filter { !status.contains(it) }.toTypedArray())
    }
}
