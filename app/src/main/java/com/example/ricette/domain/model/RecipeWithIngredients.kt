package com.example.ricette.domain.model

data class RecipeWithIngredients(
    val recipe: Recipe,
    val ingredients: List<RecipeIngredientWithFoodItem>
)
