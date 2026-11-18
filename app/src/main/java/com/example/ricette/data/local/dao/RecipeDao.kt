package com.example.ricette.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ricette.data.local.entity.RecipeEntity
import com.example.ricette.data.local.entity.RecipeIngredientEntity
import com.example.ricette.data.local.entity.RecipeIngredientWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY title ASC")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipeById(id: Long): Flow<RecipeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Update
    suspend fun updateRecipe(recipe: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    @Query(
        """
        SELECT 
            ri.recipeId, 
            ri.foodItemId, 
            ri.amountInGrams, 
            fi.name, 
            fi.kcalPer100g, 
            fi.carbsPer100g, 
            fi.proteinPer100g, 
            fi.fatPer100g, 
            fi.isDairyFree, 
            fi.isGlutenFree 
        FROM recipe_ingredients ri
        INNER JOIN food_items fi ON ri.foodItemId = fi.id
        WHERE ri.recipeId = :recipeId
        ORDER BY fi.name ASC
        """
    )
    fun getIngredientsForRecipe(recipeId: Long): Flow<List<RecipeIngredientWithDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeIngredient(recipeIngredient: RecipeIngredientEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeIngredients(recipeIngredients: List<RecipeIngredientEntity>)

    @Query("DELETE FROM recipe_ingredients WHERE recipeId = :recipeId AND foodItemId = :foodItemId")
    suspend fun deleteRecipeIngredient(recipeId: Long, foodItemId: Long)

    @Query("DELETE FROM recipe_ingredients WHERE recipeId = :recipeId")
    suspend fun deleteIngredientsForRecipe(recipeId: Long)
}
