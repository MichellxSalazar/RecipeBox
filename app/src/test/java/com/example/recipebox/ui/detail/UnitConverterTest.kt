package com.example.recipebox.ui.detail

import org.junit.Assert.assertEquals
import org.junit.Test

class UnitConverterTest {

    @Test
    fun gramsToOunces_isCorrect() {
        val grams = 100.0
        val expectedOunces = 3.5274
        val result = UnitConverter.gramsToOunces(grams)
        assertEquals(expectedOunces, result, 0.001)
    }

    @Test
    fun mlToCups_isCorrect() {
        val ml = 236.588
        val expectedCups = 1.0
        val result = UnitConverter.mlToCups(ml)
        assertEquals(expectedCups, result, 0.001)
    }
}