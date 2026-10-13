package com.example.recipebox.ui.detail

object UnitConverter {
    fun gramsToOunces(grams: Double): Double = grams * 0.035274
    fun ouncesToGrams(ounces: Double): Double = ounces / 0.035274
    fun mlToCups(ml: Double): Double = ml / 236.588
    fun cupsToMl(cups: Double): Double = cups * 236.588
}