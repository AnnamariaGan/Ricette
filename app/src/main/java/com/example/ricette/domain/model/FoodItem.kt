package com.example.ricette.domain.model

data class FoodItem(
    val id: Long = 0,
    val name: String,
    val kcalPer100g: Double,
    val carbsPer100g: Double,
    val proteinPer100g: Double,
    val fatPer100g: Double,
    val isDairyFree: Boolean,
    val isGlutenFree: Boolean
)
