package com.example.ricette.domain.model

data class RecipeIngredientWithFoodItem(
    val recipeId: Long,
    val foodItem: FoodItem,
    val amountInGrams: Double
)
