package com.trackomunda.domain

import com.trackomunda.hexagonal.core.domain.Ganger
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GangerTest {


    @Test
    fun testApplyingDamage() {
        val gangerWithMultipleWounds: Ganger = createAGanger().copy(wounds = 4).reset()

        gangerWithMultipleWounds.applyDamage(1)
        assertEquals(gangerWithMultipleWounds.wounds - 1, gangerWithMultipleWounds.currentWounds)
        assertFalse(gangerWithMultipleWounds.isSeriouslyInjured, "Ganger has more than one remaining wound and is therefore not injured")

        gangerWithMultipleWounds.applyDamage(gangerWithMultipleWounds.currentWounds)
        assertEquals(0, gangerWithMultipleWounds.currentWounds)
        assertTrue(gangerWithMultipleWounds.isSeriouslyInjured)

        gangerWithMultipleWounds.applyDamage(Integer.MAX_VALUE)
        assertEquals(0, gangerWithMultipleWounds.currentWounds, "Can't get below zero Wounds")
        assertTrue(gangerWithMultipleWounds.isSeriouslyInjured)
    }


    private fun createAGanger(


    ) = Ganger(
        name = "Test Ganger",
        movement = "5",
        weaponSkill = "5",
        ballisticSkill = "5",
        strength = "5",
        toughness = 3,
        wounds = 1,
        initiative = "5",
        attacks = "3",
        leadership = 8,
        coolness = 8,
        willpower = 8,
        intelligence = 8,
    )

}