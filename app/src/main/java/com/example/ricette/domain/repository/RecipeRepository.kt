package com.example.ricette.domain.repository

import com.example.ricette.domain.model.FoodItem
import com.example.ricette.domain.model.Recipe
import com.example.ricette.domain.model.RecipeIngredient
import com.example.ricette.domain.model.RecipeWithIngredients
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getAllRecipes(): Flow<List<Recipe>>
    fun getAllRecipesWithIngredients(): Flow<List<RecipeWithIngredients>>
    fun getRecipeById(id: Long): Flow<Recipe?>
    fun getRecipeWithIngredients(recipeId: Long): Flow<RecipeWithIngredients?>
    
    fun getAllFoodItems(): Flow<List<FoodItem>>
    fun getFoodItemById(id: Long): Flow<FoodItem?>
    fun searchFoodItems(query: String): Flow<List<FoodItem>>

    suspend fun insertRecipe(recipe: Recipe): Long
    suspend fun updateRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipe: Recipe)

    suspend fun insertFoodItem(foodItem: FoodItem): Long
    suspend fun updateFoodItem(foodItem: FoodItem)
    suspend fun deleteFoodItem(foodItem: FoodItem)

    suspend fun addIngredientToRecipe(recipeIngredient: RecipeIngredient)
    suspend fun removeIngredientFromRecipe(recipeId: Long, foodItemId: Long)
    suspend fun updateRecipeIngredients(recipeId: Long, ingredients: List<RecipeIngredient>)
}
