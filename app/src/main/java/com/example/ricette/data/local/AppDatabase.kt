package com.example.ricette.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ricette.data.local.dao.FoodItemDao
import com.example.ricette.data.local.dao.RecipeDao
import com.example.ricette.data.local.entity.FoodItemEntity
import com.example.ricette.data.local.entity.RecipeEntity
import com.example.ricette.data.local.entity.RecipeIngredientEntity

@Database(
    entities = [
        FoodItemEntity::class,
        RecipeEntity::class,
        RecipeIngredientEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodItemDao(): FoodItemDao
    abstract fun recipeDao(): RecipeDao

    companion object {
        const val DATABASE_NAME = "ricette_database"
    }
}
