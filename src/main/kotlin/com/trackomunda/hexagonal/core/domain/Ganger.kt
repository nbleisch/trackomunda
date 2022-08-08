package com.trackomunda.hexagonal.core.domain

import kotlin.properties.Delegates

data class Ganger(
    val name: String,
) {
    var isOutOfAction: Boolean by Delegates.notNull()
    var isReady: Boolean by Delegates.notNull()
    var isPartOfCrew: Boolean by Delegates.notNull()
    var isSeriouslyInjured: Boolean by Delegates.notNull()

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