package com.example.ricette.data.local.entity

import com.example.ricette.domain.model.FoodItem
import com.example.ricette.domain.model.RecipeIngredientWithFoodItem

data class RecipeIngredientWithDetails(
    val recipeId: Long,
    val foodItemId: Long,
    val amountInGrams: Double,
    val name: String,
    val kcalPer100g: Double,
    val carbsPer100g: Double,
    val proteinPer100g: Double,
    val fatPer100g: Double,
    val isDairyFree: Boolean,
    val isGlutenFree: Boolean
) {
    fun toDomain(): RecipeIngredientWithFoodItem = RecipeIngredientWithFoodItem(
        recipeId = recipeId,
        foodItem = FoodItem(
            id = foodItemId,
            name = name,
            kcalPer100g = kcalPer100g,
            carbsPer100g = carbsPer100g,
            proteinPer100g = proteinPer100g,
            fatPer100g = fatPer100g,
            isDairyFree = isDairyFree,
            isGlutenFree = isGlutenFree
        ),
        amountInGrams = amountInGrams
    )
}
