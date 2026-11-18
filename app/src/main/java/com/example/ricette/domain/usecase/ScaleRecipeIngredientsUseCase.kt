package com.example.ricette.domain.usecase

import com.example.ricette.domain.model.RecipeIngredientWithFoodItem
import com.example.ricette.domain.model.ScaledIngredient
import javax.inject.Inject
import kotlin.math.round

class ScaleRecipeIngredientsUseCase @Inject constructor() {

    /**
     * Scales ingredients based on target servings relative to base servings.
     */
    fun scaleByServings(
        ingredients: List<RecipeIngredientWithFoodItem>,
        baseServings: Int,
        targetServings: Int
    ): List<ScaledIngredient> {
        if (baseServings <= 0 || targetServings <= 0) {
            return ingredients.map {
                ScaledIngredient(
                    foodItem = it.foodItem,
                    amountInGrams = roundToOneDecimal(it.amountInGrams)
                )
            }
        }

        val ratio = targetServings.toDouble() / baseServings.toDouble()
        return ingredients.map {
            ScaledIngredient(
                foodItem = it.foodItem,
                amountInGrams = roundToOneDecimal(it.amountInGrams * ratio)
            )
        }
    }

    /**
     * Scales ingredients based on a desired weight for a specific base ingredient (e.g. flour).
     */
    fun scaleByIngredientWeight(
        ingredients: List<RecipeIngredientWithFoodItem>,
        baseIngredientFoodItemId: Long,
        targetWeightInGrams: Double
    ): List<ScaledIngredient> {
        val baseIngredient = ingredients.find { it.foodItem.id == baseIngredientFoodItemId }

        if (baseIngredient == null || baseIngredient.amountInGrams <= 0.0 || targetWeightInGrams <= 0.0) {
            return ingredients.map {
                ScaledIngredient(
                    foodItem = it.foodItem,
                    amountInGrams = roundToOneDecimal(it.amountInGrams)
                )
            }
        }

        val ratio = targetWeightInGrams / baseIngredient.amountInGrams
        return ingredients.map {
            ScaledIngredient(
                foodItem = it.foodItem,
                amountInGrams = roundToOneDecimal(it.amountInGrams * ratio)
            )
        }
    }

    private fun roundToOneDecimal(value: Double): Double {
        return round(value * 10.0) / 10.0
    }
}
