package com.example.ricette.domain.model

data class RecipeIngredient(
    val recipeId: Long,
    val foodItemId: Long,
    val amountInGrams: Double
)
