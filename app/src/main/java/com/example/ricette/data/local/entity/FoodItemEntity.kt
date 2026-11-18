package com.example.ricette.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ricette.domain.model.FoodItem

@Entity(tableName = "food_items")
data class FoodItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val kcalPer100g: Double,
    val carbsPer100g: Double,
    val proteinPer100g: Double,
    val fatPer100g: Double,
    val isDairyFree: Boolean,
    val isGlutenFree: Boolean,
    val unitLabel: String = "g",
    val gramsPerUnit: Double = 1.0
) {
    fun toDomain() = FoodItem(id, name, kcalPer100g, carbsPer100g, proteinPer100g, fatPer100g, isDairyFree, isGlutenFree, unitLabel, gramsPerUnit)
    companion object {
        fun fromDomain(d: FoodItem) = FoodItemEntity(d.id, d.name, d.kcalPer100g, d.carbsPer100g, d.proteinPer100g, d.fatPer100g, d.isDairyFree, d.isGlutenFree, d.unitLabel, d.gramsPerUnit)
    }
}
