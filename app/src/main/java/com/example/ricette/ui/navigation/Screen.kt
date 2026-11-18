package com.example.ricette.ui.navigation

sealed class Screen(val route: String) {
    data object RecipeList : Screen("recipe_list")
    data object RecipeDetail : Screen("recipe_detail/{recipeId}") {
        fun createRoute(recipeId: Long): String = "recipe_detail/$recipeId"
    }
    data object AddRecipe : Screen("add_recipe")
}
