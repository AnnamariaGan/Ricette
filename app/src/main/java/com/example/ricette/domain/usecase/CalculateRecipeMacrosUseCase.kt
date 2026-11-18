package com.example.ricette.domain.usecase

import com.example.ricette.domain.model.MacroNutrients
import com.example.ricette.domain.model.RecipeIngredientWithFoodItem
import com.example.ricette.domain.model.RecipeMacros
import javax.inject.Inject
import kotlin.math.round

class CalculateRecipeMacrosUseCase @Inject constructor() {

    operator fun invoke(
        ingredients: List<RecipeIngredientWithFoodItem>,
        servings: Int
    ): RecipeMacros {
        val safeServings = if (servings > 0) servings else 1

        var totalKcal = 0.0
        var totalCarbs = 0.0
        var totalProtein = 0.0
        var totalFat = 0.0

        for (ingredient in ingredients) {
            if (ingredient.amountInGrams > 0) {
                val ratio = ingredient.amountInGrams / 100.0
                totalKcal += ingredient.foodItem.kcalPer100g * ratio
                totalCarbs += ingredient.foodItem.carbsPer100g * ratio
                totalProtein += ingredient.foodItem.proteinPer100g * ratio
                totalFat += ingredient.foodItem.fatPer100g * ratio
            }
        }

        val total = MacroNutrients(
            kcal = roundToOneDecimal(totalKcal),
            carbs = roundToOneDecimal(totalCarbs),
            protein = roundToOneDecimal(totalProtein),
            fat = roundToOneDecimal(totalFat)
        )

        val perServing = MacroNutrients(
            kcal = roundToOneDecimal(total.kcal / safeServings),
            carbs = roundToOneDecimal(total.carbs / safeServings),
            protein = roundToOneDecimal(total.protein / safeServings),
            fat = roundToOneDecimal(total.fat / safeServings)
        )

        return RecipeMacros(total = total, perServing = perServing)
    }

    private fun roundToOneDecimal(value: Double): Double {
        return round(value * 10.0) / 10.0
    }
}
