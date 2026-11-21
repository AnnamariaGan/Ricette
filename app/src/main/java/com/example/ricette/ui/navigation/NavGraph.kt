package com.example.ricette.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ricette.ui.addrecipe.AddRecipeScreen
import com.example.ricette.ui.recipedetail.RecipeDetailScreen
import com.example.ricette.ui.recipelist.RecipeListScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.RecipeList.route,
        modifier = modifier
    ) {
        composable(route = Screen.RecipeList.route) {
            RecipeListScreen(
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                },
                onAddRecipeClick = {
                    navController.navigate(Screen.AddRecipe.route)
                }
            )
        }

        composable(
            route = Screen.RecipeDetail.route,
            arguments = listOf(
                navArgument("recipeId") {
                    type = NavType.LongType
                }
            )
        ) {
            RecipeDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = { recipeId ->
                    navController.navigate(Screen.EditRecipe.createRoute(recipeId))
                }
            )
        }

        composable(route = Screen.AddRecipe.route) {
            AddRecipeScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.EditRecipe.route,
            arguments = listOf(
                navArgument("recipeId") {
                    type = NavType.LongType
                }
            )
        ) {
            AddRecipeScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
