package com.trackomunda.hexagonal.core.domain

import com.trackomunda.hexagonal.core.domain.FighterStatus.*
import java.lang.Integer.max
import kotlin.properties.Delegates

data class Ganger(
    //val identifier: String,
    val name: String,
    val movement: String,
    val weaponSkill: String,
    val ballisticSkill: String,
    val strength: String,
    val toughness: Int,
    val wounds: Int,
    val initiative: String,
    val attacks: String,
    val leadership: Int,
    val coolness: Int,
    val willpower: Int,
    val intelligence: Int,
) {
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


    fun hitWithBlaze(diceRoll: OneD6): Ganger {
        if (diceRoll >= 4) isBlazed = true
        return this
    }

    fun hitWithCurse(diceRoll: TwoD6): Ganger {
        if (diceRoll < willpower) isInsane = true
        return this
    }

    fun pin(): Ganger = requireStatus(STANDING).apply { status = PRONE }

    fun engage(): Ganger = requireStatus(STANDING, ENGAGED).apply { status = ENGAGED }

    fun standUp(): Ganger = requireStatus(STANDING, ENGAGED).apply { status = STANDING }

    fun injure(): Ganger = this.apply { status = SERIOUSLY_INJURED }

    fun recover(): Ganger = requireStatus(SERIOUSLY_INJURED).apply {
        status = PRONE
        applyFleshWound()
    }

    fun applyFleshWound(): Ganger = requireStatusNot(SERIOUSLY_INJURED).apply {
        currentToughness = max(0, currentToughness - 1)
        if (currentToughness == 0) goOutOfAction()
    }

    fun goOutOfAction(): Ganger = this.apply {
        isOutOfAction = true
    }

    fun applyDamage(damage: Int): Ganger = this.apply {
        currentWounds = max(0, currentWounds - damage)
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

    fun reset(): Ganger {
        isOutOfAction = false
        isReady = true
        isPartOfCrew = false
        isBlazed = false
        isInsane = false
        currentWounds = wounds
        currentToughness = toughness
        status = STANDING
        actionStack.clear()
        return this
    }

    private fun requireActionAvailable(action: FighterAction): Ganger =
        this.also { if (!getActionsAvailable().contains(action)) throw FighterActionNotAvailableException(action = action, ganger = this) }

    private fun requireStatus(vararg status: FighterStatus): Ganger = this.also {
        if (!status.contains(this.status)) throw RequiredFighterStatusDoesNotExistException(requiredFighterStatus = status.toList(), ganger = this)
    }

    private fun requireStatusNot(vararg status: FighterStatus): Ganger = this.also {
        requireStatus(*FighterStatus.values().toList().filter { !status.contains(it) }.toTypedArray())
    }
}