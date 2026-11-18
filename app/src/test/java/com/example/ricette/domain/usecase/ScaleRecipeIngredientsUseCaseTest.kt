package com.example.ricette.domain.usecase

import com.example.ricette.domain.model.FoodItem
import com.example.ricette.domain.model.RecipeIngredientWithFoodItem
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ScaleRecipeIngredientsUseCaseTest {

    private lateinit var useCase: ScaleRecipeIngredientsUseCase

    private val flour00 = FoodItem(
        id = 1,
        name = "Farina 00",
        kcalPer100g = 343.0,
        carbsPer100g = 73.0,
        proteinPer100g = 11.0,
        fatPer100g = 1.0,
        isDairyFree = true,
        isGlutenFree = false
    )

    private val water = FoodItem(
        id = 5,
        name = "Acqua",
        kcalPer100g = 0.0,
        carbsPer100g = 0.0,
        proteinPer100g = 0.0,
        fatPer100g = 0.0,
        isDairyFree = true,
        isGlutenFree = true
    )

    private val yeast = FoodItem(
        id = 6,
        name = "Lievito Fresco",
        kcalPer100g = 96.0,
        carbsPer100g = 1.5,
        proteinPer100g = 12.5,
        fatPer100g = 1.5,
        isDairyFree = true,
        isGlutenFree = true
    )

    private val sampleIngredients = listOf(
        RecipeIngredientWithFoodItem(recipeId = 1, foodItem = flour00, amountInGrams = 500.0),
        RecipeIngredientWithFoodItem(recipeId = 1, foodItem = water, amountInGrams = 325.0),
        RecipeIngredientWithFoodItem(recipeId = 1, foodItem = yeast, amountInGrams = 10.0)
    )

    @Before
    fun setUp() {
        useCase = ScaleRecipeIngredientsUseCase()
    }

    @Test
    fun `scaleByServings doubles ingredients when scaling from 2 to 4 servings`() {
        val scaled = useCase.scaleByServings(sampleIngredients, baseServings = 2, targetServings = 4)

        assertEquals(3, scaled.size)
        assertEquals(1000.0, scaled[0].amountInGrams, 0.1) // 500 * 2
        assertEquals(650.0, scaled[1].amountInGrams, 0.1)  // 325 * 2
        assertEquals(20.0, scaled[2].amountInGrams, 0.1)   // 10 * 2
    }

    @Test
    fun `scaleByServings halves ingredients when scaling from 4 to 2 servings`() {
        val scaled = useCase.scaleByServings(sampleIngredients, baseServings = 4, targetServings = 2)

        assertEquals(3, scaled.size)
        assertEquals(250.0, scaled[0].amountInGrams, 0.1) // 500 / 2
        assertEquals(162.5, scaled[1].amountInGrams, 0.1) // 325 / 2
        assertEquals(5.0, scaled[2].amountInGrams, 0.1)   // 10 / 2
    }

    @Test
    fun `scaleByServings handles invalid servings safely`() {
        val scaledZeroTarget = useCase.scaleByServings(sampleIngredients, baseServings = 4, targetServings = 0)
        assertEquals(500.0, scaledZeroTarget[0].amountInGrams, 0.1)

        val scaledNegativeBase = useCase.scaleByServings(sampleIngredients, baseServings = -1, targetServings = 4)
        assertEquals(500.0, scaledNegativeBase[0].amountInGrams, 0.1)
    }

    @Test
    fun `scaleByIngredientWeight scales proportionally when flour weight changes from 500g to 750g`() {
        // Scaling factor: 750 / 500 = 1.5
        val scaled = useCase.scaleByIngredientWeight(
            ingredients = sampleIngredients,
            baseIngredientFoodItemId = 1L, // flour
            targetWeightInGrams = 750.0
        )

        assertEquals(3, scaled.size)
        assertEquals(750.0, scaled[0].amountInGrams, 0.1)
        assertEquals(487.5, scaled[1].amountInGrams, 0.1) // 325 * 1.5
        assertEquals(15.0, scaled[2].amountInGrams, 0.1)  // 10 * 1.5
    }

    @Test
    fun `scaleByIngredientWeight returns original amounts if ingredient not found or target weight invalid`() {
        val scaledNotFound = useCase.scaleByIngredientWeight(
            ingredients = sampleIngredients,
            baseIngredientFoodItemId = 999L,
            targetWeightInGrams = 1000.0
        )
        assertEquals(500.0, scaledNotFound[0].amountInGrams, 0.1)

        val scaledInvalidWeight = useCase.scaleByIngredientWeight(
            ingredients = sampleIngredients,
            baseIngredientFoodItemId = 1L,
            targetWeightInGrams = -50.0
        )
        assertEquals(500.0, scaledInvalidWeight[0].amountInGrams, 0.1)
    }
}
