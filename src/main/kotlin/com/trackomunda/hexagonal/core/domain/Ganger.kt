package com.trackomunda.hexagonal.core.domain

import kotlin.math.min
import kotlin.properties.Delegates

data class Ganger(
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
    var isSeriouslyInjured: Boolean by Delegates.notNull()
        private set
    var isBlazed: Boolean by Delegates.notNull()
        private set
    var isInsane: Boolean by Delegates.notNull()
        private set
    var currentWounds: Int by Delegates.notNull()
        private set
    var currentToughness: Int by Delegates.notNull()
        private set


    init {
        reset()
    }

    fun hitWithBlaze(diceRoll: OneD6) {
        if (diceRoll >= 4) isBlazed = true
    }

    fun hitWithCurse(diceRoll: TwoD6) {
        if (diceRoll < willpower) isInsane = true
    }

    fun applyDamage(damage: Int) {
        currentWounds = min(0, currentWounds - damage)
        if(currentWounds == 0){
            isSeriouslyInjured = true
        }
    }

    fun reset() {
        isOutOfAction = false
        isReady = true
        isPartOfCrew = false
        isSeriouslyInjured = false
        isBlazed = false
        isInsane = false
        currentWounds = wounds
        currentToughness = toughness
    }
}