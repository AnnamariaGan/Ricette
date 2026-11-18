package com.example.ricette.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ricette.domain.model.Recipe

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String,
    val baseServings: Int,
    val prepTimeMinutes: Int,
    val instructions: String,
    val personalNotes: String
) {
    fun toDomain(): Recipe = Recipe(
        id = id,
        title = title,
        category = category,
        baseServings = baseServings,
        prepTimeMinutes = prepTimeMinutes,
        instructions = instructions,
        personalNotes = personalNotes
    )

    companion object {
        fun fromDomain(domain: Recipe): RecipeEntity = RecipeEntity(
            id = domain.id,
            title = domain.title,
            category = domain.category,
            baseServings = domain.baseServings,
            prepTimeMinutes = domain.prepTimeMinutes,
            instructions = domain.instructions,
            personalNotes = domain.personalNotes
        )
    }
}
