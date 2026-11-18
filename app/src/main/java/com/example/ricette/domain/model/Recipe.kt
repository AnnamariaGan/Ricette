package com.example.ricette.domain.model

data class Recipe(
    val id: Long = 0,
    val title: String,
    val category: String,
    val baseServings: Int,
    val prepTimeMinutes: Int,
    val instructions: String,
    val personalNotes: String
)
