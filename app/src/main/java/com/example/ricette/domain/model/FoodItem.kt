package com.example.ricette.domain.model

import kotlin.math.round

data class FoodItem(
    val id: Long = 0,
    val name: String,
    val kcalPer100g: Double,
    val carbsPer100g: Double,
    val proteinPer100g: Double,
    val fatPer100g: Double,
    val isDairyFree: Boolean,
    val isGlutenFree: Boolean,
    val unitLabel: String = "g",
    val gramsPerUnit: Double = 1.0
) {
    fun formatAmount(amountInGrams: Double): String {
        if (unitLabel == "g" || gramsPerUnit <= 0.0) {
            return if (amountInGrams % 1.0 == 0.0) "${amountInGrams.toInt()} g"
            else "${round(amountInGrams * 10.0) / 10.0} g"
        }
        val raw = amountInGrams / gramsPerUnit
        val units = when (unitLabel) {
            "uova", "cipolle", "spicchi" -> maxOf(1.0, round(raw))
            else -> round(raw * 2.0) / 2.0
        }
        val lbl = when {
            unitLabel == "uova" && units == 1.0 -> "uovo"
            unitLabel == "cipolle" && units == 1.0 -> "cipolla"
            unitLabel == "spicchi" && units == 1.0 -> "spicchio"
            unitLabel == "cucchiai" && units == 1.0 -> "cucchiaio"
            else -> unitLabel
        }
        val n = if (units % 1.0 == 0.0) "${units.toInt()}" else "$units"
        return "$n $lbl"
    }
}
