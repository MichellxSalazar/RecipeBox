package com.example.recipebox.ui.modococina

import com.example.recipebox.ui.modococina.utils.UnitConverter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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

    @Test
    fun convertIngredientMeasurement_metricToImperial_convertsGramsToOunces() {
        val input = "100 g de Harina de trigo"
        val result = UnitConverter.convertIngredientMeasurement(input, useMetric = false)
        assertTrue("Debe contener 'oz': $result", result.contains("oz"))
    }

    @Test
    fun convertIngredientMeasurement_imperialToMetric_convertsOuncesToGrams() {
        val input = "3.5 oz de Harina de trigo"
        val result = UnitConverter.convertIngredientMeasurement(input, useMetric = true)
        assertTrue("Debe contener 'g': $result", result.contains("g"))
    }

    @Test
    fun convertIngredientMeasurement_leavesCountUnchanged() {
        val input = "2 unidades de Huevos"
        val result = UnitConverter.convertIngredientMeasurement(input, useMetric = false)
        assertEquals("2 unidades de Huevos", result)
    }
}
