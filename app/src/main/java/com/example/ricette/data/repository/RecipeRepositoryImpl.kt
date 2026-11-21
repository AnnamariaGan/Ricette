package com.example.ricette.data.repository

import com.example.ricette.data.local.dao.FoodItemDao
import com.example.ricette.data.local.dao.RecipeDao
import com.example.ricette.data.local.entity.FoodItemEntity
import com.example.ricette.data.local.entity.RecipeEntity
import com.example.ricette.data.local.entity.RecipeIngredientEntity
import com.example.ricette.domain.model.FoodItem
import com.example.ricette.domain.model.Recipe
import com.example.ricette.domain.model.RecipeIngredient
import com.example.ricette.domain.model.RecipeWithIngredients
import com.example.ricette.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val foodItemDao: FoodItemDao,
    private val recipeDao: RecipeDao
) : RecipeRepository {

    override fun getAllRecipes(): Flow<List<Recipe>> {
        return recipeDao.getAllRecipes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllRecipesWithIngredients(): Flow<List<RecipeWithIngredients>> {
        return combine(
            recipeDao.getAllRecipes(),
            recipeDao.getAllRecipeIngredients()
        ) { recipes, ingredients ->
            val ingredientsByRecipe = ingredients.groupBy { it.recipeId }
            recipes.map { recipeEntity ->
                RecipeWithIngredients(
                    recipe = recipeEntity.toDomain(),
                    ingredients = ingredientsByRecipe[recipeEntity.id]?.map { it.toDomain() } ?: emptyList()
                )
            }
        }
    }

    override fun getRecipeById(id: Long): Flow<Recipe?> {
        return recipeDao.getRecipeById(id).map { it?.toDomain() }
    }

    override fun getRecipeWithIngredients(recipeId: Long): Flow<RecipeWithIngredients?> {
        return combine(
            recipeDao.getRecipeById(recipeId),
            recipeDao.getIngredientsForRecipe(recipeId)
        ) { recipeEntity, ingredientDetails ->
            recipeEntity?.let { recipe ->
                RecipeWithIngredients(
                    recipe = recipe.toDomain(),
                    ingredients = ingredientDetails.map { it.toDomain() }
                )
            }
        }
    }

    override fun getAllFoodItems(): Flow<List<FoodItem>> {
        return foodItemDao.getAllFoodItems().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getFoodItemById(id: Long): Flow<FoodItem?> {
        return foodItemDao.getFoodItemById(id).map { it?.toDomain() }
    }

    override fun searchFoodItems(query: String): Flow<List<FoodItem>> {
        return foodItemDao.searchFoodItems(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertRecipe(recipe: Recipe): Long {
        return recipeDao.insertRecipe(RecipeEntity.fromDomain(recipe))
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        recipeDao.updateRecipe(RecipeEntity.fromDomain(recipe))
    }

    override suspend fun updateFavoriteStatus(recipeId: Long, isFavorite: Boolean) {
        recipeDao.updateFavoriteStatus(recipeId, isFavorite)
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(RecipeEntity.fromDomain(recipe))
    }

    override suspend fun insertFoodItem(foodItem: FoodItem): Long {
        return foodItemDao.insertFoodItem(FoodItemEntity.fromDomain(foodItem))
    }

    override suspend fun updateFoodItem(foodItem: FoodItem) {
        foodItemDao.updateFoodItem(FoodItemEntity.fromDomain(foodItem))
    }

    override suspend fun deleteFoodItem(foodItem: FoodItem) {
        foodItemDao.deleteFoodItem(FoodItemEntity.fromDomain(foodItem))
    }

    override suspend fun addIngredientToRecipe(recipeIngredient: RecipeIngredient) {
        recipeDao.insertRecipeIngredient(RecipeIngredientEntity.fromDomain(recipeIngredient))
    }

    override suspend fun removeIngredientFromRecipe(recipeId: Long, foodItemId: Long) {
        recipeDao.deleteRecipeIngredient(recipeId = recipeId, foodItemId = foodItemId)
    }

    override suspend fun updateRecipeIngredients(recipeId: Long, ingredients: List<RecipeIngredient>) {
        recipeDao.deleteIngredientsForRecipe(recipeId)
        recipeDao.insertRecipeIngredients(ingredients.map { RecipeIngredientEntity.fromDomain(it) })
    }
}
