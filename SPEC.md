# Project Specification: Grammi

## Goal
An offline-first Android recipe manager focused on homemade baking and cooking, featuring dynamic portion scaling and automatic macronutrient calculation (kcal, carbs, proteins, fats).

## Tech Stack & Architecture
- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Architecture:** MVVM + Clean Architecture principles (UI -> ViewModel -> Repository -> Room Database)
- **Dependency Injection:** Hilt
- **Async:** Kotlin Coroutines & StateFlow
- **Database:** Room (SQLite)

## Core Domain Model
1. `FoodItem`: Base ingredient (id, name, kcalPer100g, carbsPer100g, proteinPer100g, fatPer100g, isDairyFree, isGlutenFree).
2. `Recipe`: (id, title, category, baseServings, prepTimeMinutes, instructions, personalNotes).
3. `RecipeIngredient`: Cross-reference joining `Recipe` and `FoodItem` with `amountInGrams`.

## Agent Rules
- Always write clean, idiomatic Kotlin.
- Keep business logic (macro calculations and portion scaling) inside pure Kotlin UseCases so they can be easily unit-tested with JUnit.
- After modifying Gradle files or Kotlin code, run `./gradlew assembleDebug` in the terminal to verify the build succeeds before finishing a task.