package com.example.ricette.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ricette.domain.model.FoodItem

@Entity(tableName = "food_items")
data class FoodItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val kcalPer100g: Double,
    val carbsPer100g: Double,
    val proteinPer100g: Double,
    val fatPer100g: Double,
    val isDairyFree: Boolean,
    val isGlutenFree: Boolean
) {
    fun toDomain(): FoodItem = FoodItem(
        id = id,
        name = name,
        kcalPer100g = kcalPer100g,
        carbsPer100g = carbsPer100g,
        proteinPer100g = proteinPer100g,
        fatPer100g = fatPer100g,
        isDairyFree = isDairyFree,
        isGlutenFree = isGlutenFree
    )

    companion object {
        fun fromDomain(domain: FoodItem): FoodItemEntity = FoodItemEntity(
            id = domain.id,
            name = domain.name,
            kcalPer100g = domain.kcalPer100g,
            carbsPer100g = domain.carbsPer100g,
            proteinPer100g = domain.proteinPer100g,
            fatPer100g = domain.fatPer100g,
            isDairyFree = domain.isDairyFree,
            isGlutenFree = domain.isGlutenFree
        )
    }
}
