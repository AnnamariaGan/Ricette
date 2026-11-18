package com.example.ricette.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.ricette.domain.model.RecipeIngredient

@Entity(
    tableName = "recipe_ingredients",
    primaryKeys = ["recipeId", "foodItemId"],
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FoodItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["foodItemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("recipeId"),
        Index("foodItemId")
    ]
)
data class RecipeIngredientEntity(
    val recipeId: Long,
    val foodItemId: Long,
    val amountInGrams: Double
) {
    fun toDomain(): RecipeIngredient = RecipeIngredient(
        recipeId = recipeId,
        foodItemId = foodItemId,
        amountInGrams = amountInGrams
    )

    companion object {
        fun fromDomain(domain: RecipeIngredient): RecipeIngredientEntity = RecipeIngredientEntity(
            recipeId = domain.recipeId,
            foodItemId = domain.foodItemId,
            amountInGrams = domain.amountInGrams
        )
    }
}
