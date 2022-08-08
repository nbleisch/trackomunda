package com.trackomunda.hexagonal.core.domain

data class Gang(
    val name: String,
    val ganger: List<Ganger>,

    var isBottleCheckWasRequired: Boolean = false
) {
    val startingCrewSize: Int
        get() {
            return ganger.filter { it.isPartOfCrew }.size
        }

    fun calculateBottleCheckModifier() = ganger.filter { it.isPartOfCrew && (it.isOutOfAction || it.isSeriouslyInjured) }.size

    internal fun noFighterIsReady(): Boolean {
        TODO("Not yet implemented")
    }

    internal fun makeAllFightersReady() {
        TODO("Not yet implemented")
    }

    fun reset() {
        ganger.forEach { ganger ->
            ganger.reset()
        }
    }
}