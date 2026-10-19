package com.example.recipebox.ui.modococina.utils

import java.util.Locale

object UnitConverter {
    fun gramsToOunces(grams: Double): Double = grams * 0.035274
    fun ouncesToGrams(ounces: Double): Double = ounces / 0.035274
    fun mlToCups(ml: Double): Double = ml / 236.588
    fun cupsToMl(cups: Double): Double = cups * 236.588

    fun kgToPounds(kg: Double): Double = kg * 2.20462
    fun poundsToKg(lb: Double): Double = lb / 2.20462
    fun mlToFlOz(ml: Double): Double = ml * 0.033814
    fun flOzToMl(flOz: Double): Double = flOz / 0.033814

    /**
     * Convierte una cadena de ingrediente (por ejemplo "200 g de Harina" o "250 ml de Leche")
     * al sistema indicado por [useMetric].
     * Si useMetric es true -> convierte unidades imperiales (oz, lb, cups/tazas, fl oz) al sistema métrico (g, kg, ml).
     * Si useMetric es false -> convierte unidades métricas (g, kg, ml, l) al sistema imperial (oz, lb, tazas, fl oz).
     */
    fun convertIngredientMeasurement(ingredientText: String, useMetric: Boolean): String {
        if (ingredientText.isBlank()) return ingredientText

        val regex = Regex(
            """(\d+(?:[.,]\d+)?)\s*(gramos|gramo|gr|g|kilogramos|kilogramo|kilos|kilo|kg|mililitros|mililitro|ml|litros|litro|l|onzas|onza|oz|libras|libra|lb|tazas|taza|cups|cup|fl\s*oz)\b""",
            RegexOption.IGNORE_CASE
        )

        return regex.replace(ingredientText) { matchResult ->
            val numStr = matchResult.groupValues[1].replace(',', '.')
            val unitStr = matchResult.groupValues[2].lowercase().replace(" ", "")
            val value = numStr.toDoubleOrNull() ?: return@replace matchResult.value

            if (!useMetric) {
                // Convertir de Métrico a Imperial
                when (unitStr) {
                    "g", "gr", "gramo", "gramos" -> {
                        val oz = gramsToOunces(value)
                        formatNumber(oz) + " oz"
                    }
                    "kg", "kilo", "kilos", "kilogramo", "kilogramos" -> {
                        val lb = kgToPounds(value)
                        formatNumber(lb) + " lb"
                    }
                    "ml", "mililitro", "mililitros" -> {
                        val cups = mlToCups(value)
                        if (cups >= 0.25) {
                            formatNumber(cups) + " tazas"
                        } else {
                            val flOz = mlToFlOz(value)
                            formatNumber(flOz) + " fl oz"
                        }
                    }
                    "l", "litro", "litros" -> {
                        val cups = mlToCups(value * 1000.0)
                        formatNumber(cups) + " tazas"
                    }
                    else -> matchResult.value
                }
            } else {
                // Convertir de Imperial a Métrico
                when (unitStr) {
                    "oz", "onza", "onzas" -> {
                        val g = ouncesToGrams(value)
                        formatNumber(g) + " g"
                    }
                    "lb", "libra", "libras" -> {
                        val kg = poundsToKg(value)
                        if (kg < 1.0) {
                            formatNumber(kg * 1000.0) + " g"
                        } else {
                            formatNumber(kg) + " kg"
                        }
                    }
                    "cup", "cups", "taza", "tazas" -> {
                        val ml = cupsToMl(value)
                        formatNumber(ml) + " ml"
                    }
                    "floz" -> {
                        val ml = flOzToMl(value)
                        formatNumber(ml) + " ml"
                    }
                    else -> matchResult.value
                }
            }
        }
    }

    private fun formatNumber(value: Double): String {
        return if (value >= 10.0) {
            String.format(Locale.US, "%.0f", value)
        } else {
            val formatted = String.format(Locale.US, "%.1f", value)
            if (formatted.endsWith(".0")) formatted.substringBefore(".0") else formatted
        }
    }

    /**
     * Convierte un texto con varios ingredientes (separados por coma o salto de línea)
     */
    fun convertIngredientsText(ingredientsText: String, useMetric: Boolean): String {
        val delimiter = if (ingredientsText.contains("\n")) "\n" else ", "
        return ingredientsText.split("\n", ",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .joinToString(delimiter) { convertIngredientMeasurement(it, useMetric) }
    }
}
