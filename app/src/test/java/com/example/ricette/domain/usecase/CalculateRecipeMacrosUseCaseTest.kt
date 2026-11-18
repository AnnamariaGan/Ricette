package com.example.ricette.domain.usecase

import com.example.ricette.domain.model.FoodItem
import com.example.ricette.domain.model.RecipeIngredientWithFoodItem
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculateRecipeMacrosUseCaseTest {

    private lateinit var useCase: CalculateRecipeMacrosUseCase

    @Before
    fun setUp() {
        useCase = CalculateRecipeMacrosUseCase()
    }

    @Test
    fun `calculate macros for single ingredient correctly`() {
        val chicken = FoodItem(
            id = 1,
            name = "Petto di Pollo",
            kcalPer100g = 110.0,
            carbsPer100g = 0.0,
            proteinPer100g = 23.0,
            fatPer100g = 1.5,
            isDairyFree = true,
            isGlutenFree = true
        )

        val ingredients = listOf(
            RecipeIngredientWithFoodItem(
                recipeId = 1,
                foodItem = chicken,
                amountInGrams = 200.0
            )
        )

        val result = useCase(ingredients, servings = 2)

        // 200g of chicken = 2 * 100g
        assertEquals(220.0, result.total.kcal, 0.1)
        assertEquals(0.0, result.total.carbs, 0.1)
        assertEquals(46.0, result.total.protein, 0.1)
        assertEquals(3.0, result.total.fat, 0.1)

        // Per serving (2 servings)
        assertEquals(110.0, result.perServing.kcal, 0.1)
        assertEquals(0.0, result.perServing.carbs, 0.1)
        assertEquals(23.0, result.perServing.protein, 0.1)
        assertEquals(1.5, result.perServing.fat, 0.1)
    }

    @Test
    fun `calculate macros for multiple ingredients correctly`() {
        val pasta = FoodItem(
            id = 15,
            name = "Pasta di Semola",
            kcalPer100g = 353.0,
            carbsPer100g = 72.0,
            proteinPer100g = 13.0,
            fatPer100g = 1.5,
            isDairyFree = true,
            isGlutenFree = false
        )
        val pomodoro = FoodItem(
            id = 10,
            name = "Pomodori Pelati",
            kcalPer100g = 21.0,
            carbsPer100g = 3.5,
            proteinPer100g = 1.2,
            fatPer100g = 0.2,
            isDairyFree = true,
            isGlutenFree = true
        )
        val olio = FoodItem(
            id = 7,
            name = "Olio EVO",
            kcalPer100g = 884.0,
            carbsPer100g = 0.0,
            proteinPer100g = 0.0,
            fatPer100g = 100.0,
            isDairyFree = true,
            isGlutenFree = true
        )

        val ingredients = listOf(
            RecipeIngredientWithFoodItem(recipeId = 2, foodItem = pasta, amountInGrams = 180.0),
            RecipeIngredientWithFoodItem(recipeId = 2, foodItem = pomodoro, amountInGrams = 250.0),
            RecipeIngredientWithFoodItem(recipeId = 2, foodItem = olio, amountInGrams = 20.0)
        )

        val result = useCase(ingredients, servings = 2)

        // Expected totals:
        // Pasta (180g): 635.4 kcal, 129.6 carbs, 23.4 protein, 2.7 fat
        // Pomodoro (250g): 52.5 kcal, 8.75 carbs, 3.0 protein, 0.5 fat
        // Olio (20g): 176.8 kcal, 0 carbs, 0 protein, 20.0 fat
        // Sums: kcal = 864.7, carbs = 138.35 -> 138.4, protein = 26.4, fat = 23.2
        assertEquals(864.7, result.total.kcal, 0.2)
        assertEquals(138.4, result.total.carbs, 0.2)
        assertEquals(26.4, result.total.protein, 0.2)
        assertEquals(23.2, result.total.fat, 0.2)

        // Per serving (2 servings):
        assertEquals(432.4, result.perServing.kcal, 0.2)
        assertEquals(69.2, result.perServing.carbs, 0.2)
        assertEquals(13.2, result.perServing.protein, 0.2)
        assertEquals(11.6, result.perServing.fat, 0.2)
    }

    @Test
    fun `empty ingredients list returns zero macros`() {
        val result = useCase(emptyList(), servings = 4)

        assertEquals(0.0, result.total.kcal, 0.01)
        assertEquals(0.0, result.total.carbs, 0.01)
        assertEquals(0.0, result.total.protein, 0.01)
        assertEquals(0.0, result.total.fat, 0.01)

        assertEquals(0.0, result.perServing.kcal, 0.01)
        assertEquals(0.0, result.perServing.carbs, 0.01)
        assertEquals(0.0, result.perServing.protein, 0.01)
        assertEquals(0.0, result.perServing.fat, 0.01)
    }

    @Test
    fun `zero or negative servings safely treated as 1 serving`() {
        val flour = FoodItem(
            id = 1,
            name = "Farina 00",
            kcalPer100g = 343.0,
            carbsPer100g = 73.0,
            proteinPer100g = 11.0,
            fatPer100g = 1.0,
            isDairyFree = true,
            isGlutenFree = false
        )

        val ingredients = listOf(
            RecipeIngredientWithFoodItem(recipeId = 1, foodItem = flour, amountInGrams = 100.0)
        )

        val resultZero = useCase(ingredients, servings = 0)
        assertEquals(resultZero.total.kcal, resultZero.perServing.kcal, 0.01)

        val resultNegative = useCase(ingredients, servings = -2)
        assertEquals(resultNegative.total.kcal, resultNegative.perServing.kcal, 0.01)
    }
}
