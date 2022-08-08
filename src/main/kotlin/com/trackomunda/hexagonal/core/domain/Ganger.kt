package com.trackomunda.hexagonal.core.domain

import kotlin.properties.Delegates

data class Ganger(
    val name: String,
    val movement: String,
    val weaponSkill: String,
    val ballisticSkill: String,
    val strength: String,
    val toughness: String,
    val wounds: Int,
    val initiative: String,
    val attacks: String,
    val leadership: String,
    val coolness: String,
    val willpower: String,
    val intelligence: String,
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
    var currentWounds: Int = wounds
        private set

    init {
        reset()
    }

    fun reset() {
        isOutOfAction = false
        isReady = true
        isPartOfCrew = false
        isSeriouslyInjured = false
    }
}