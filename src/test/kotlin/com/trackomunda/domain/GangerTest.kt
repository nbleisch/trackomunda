package com.trackomunda.domain

import com.trackomunda.hexagonal.core.domain.*
import com.trackomunda.hexagonal.core.domain.FighterStatus.SERIOUSLY_INJURED
import org.junit.Test
import java.lang.Integer.MAX_VALUE
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class GangerTest {


    @Test
    fun testApplyingDamage() {
        val gangerWithMultipleWounds: Ganger = createAGanger().copy(wounds = 4)

        gangerWithMultipleWounds.applyDamage(1)
        assertEquals(gangerWithMultipleWounds.wounds - 1, gangerWithMultipleWounds.currentWounds)
        assertNotEquals(SERIOUSLY_INJURED, gangerWithMultipleWounds.status, "Ganger has more than one remaining wound and is therefore not injured")

        gangerWithMultipleWounds.applyDamage(gangerWithMultipleWounds.currentWounds)
        assertEquals(0, gangerWithMultipleWounds.currentWounds)
        assertEquals(SERIOUSLY_INJURED, gangerWithMultipleWounds.status)

        gangerWithMultipleWounds.applyDamage(MAX_VALUE)
        assertEquals(0, gangerWithMultipleWounds.currentWounds, "Can't get below zero Wounds")
        assertEquals(SERIOUSLY_INJURED, gangerWithMultipleWounds.status)
    }

    @Test
    fun testBlazeStatusEffects() {
        val ganger: Ganger = createAGanger()
        ganger.hitWithBlaze(OneD6(1))
        assertFalse(ganger.isBlazed, "Should not be blazed")
        ganger.hitWithBlaze(OneD6(4))
        assertTrue(ganger.isBlazed, "Should be blazed")
        ganger.hitWithBlaze(OneD6(1))
        assertTrue(ganger.isBlazed, "Should be blazed")
        assertEquals(setOf<FighterAction>(AttemptToPutTheFireOut), ganger.getActionsAvailable())
    }

    @Test
    fun testInsaneStatusEffects() {
        val ganger: Ganger = createAGanger()
        ganger.hitWithCurse(TwoD6(12))
        assertFalse(ganger.isInsane, "Should not be insane")
        ganger.hitWithCurse(TwoD6(2))
        assertTrue(ganger.isInsane, "Should be insance")
        ganger.hitWithCurse(TwoD6(12))
        assertTrue(ganger.isInsane, "Should be insane")
    }

    @Test
    fun testRecoverAndOutOfAction() {
        val ganger: Ganger = createAGanger().copy(toughness = 2).injure()
        assertEquals(SERIOUSLY_INJURED, ganger.status, "Should be seriously injured")
        ganger.recover()
        assertNotEquals(SERIOUSLY_INJURED, ganger.status, "Should not be seriously injured anymore")
        assertEquals(1, ganger.currentToughness, "Toughness should be reduced by 1")
        assertFalse(ganger.isOutOfAction, "Should not be out of Action")

        ganger.injure().injure().injure()
        assertEquals(1, ganger.currentToughness, "being injured mutiple times does not touch the toughness")
        assertFalse(ganger.isOutOfAction, "Should not be out of Action")

        ganger.recover()
        assertEquals(0, ganger.currentToughness, "Toughness should now be zero")
        assertTrue(ganger.isOutOfAction, "Should be out of action because of zero toughness")

    }


    private fun createAGanger() = Ganger(
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