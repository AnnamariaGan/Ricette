package com.example.ricette.ui.recipelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ricette.domain.repository.RecipeRepository
import com.example.ricette.domain.usecase.CalculateRecipeMacrosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class RecipeItemUi(
    val id: Long,
    val title: String,
    val category: String,
    val baseServings: Int,
    val prepTimeMinutes: Int,
    val kcalPerServing: Int,
    val isDairyFree: Boolean,
    val isGlutenFree: Boolean
)

data class RecipeListUiState(
    val recipes: List<RecipeItemUi> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val onlyDairyFree: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val calculateRecipeMacrosUseCase: CalculateRecipeMacrosUseCase
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<String?>(null)
    private val _onlyDairyFree = MutableStateFlow(false)

    val uiState: StateFlow<RecipeListUiState> = combine(
        recipeRepository.getAllRecipesWithIngredients(),
        _selectedCategory,
        _onlyDairyFree
    ) { recipesWithIngredients, selectedCat, onlyDairyFree ->
        val categories = recipesWithIngredients.map { it.recipe.category }.distinct().sorted()

        val allItems = recipesWithIngredients.map { recipeWithIngredients ->
            val recipe = recipeWithIngredients.recipe
            val ingredients = recipeWithIngredients.ingredients

            val macros = calculateRecipeMacrosUseCase(ingredients, recipe.baseServings)
            val isDairyFree = ingredients.isNotEmpty() && ingredients.all { it.foodItem.isDairyFree }
            val isGlutenFree = ingredients.isNotEmpty() && ingredients.all { it.foodItem.isGlutenFree }

            RecipeItemUi(
                id = recipe.id,
                title = recipe.title,
                category = recipe.category,
                baseServings = recipe.baseServings,
                prepTimeMinutes = recipe.prepTimeMinutes,
                kcalPerServing = macros.perServing.kcal.toInt(),
                isDairyFree = isDairyFree,
                isGlutenFree = isGlutenFree
            )
        }

        val filteredItems = allItems.filter { item ->
            val matchesCategory = selectedCat == null || item.category == selectedCat
            val matchesDairy = !onlyDairyFree || item.isDairyFree
            matchesCategory && matchesDairy
        }

        RecipeListUiState(
            recipes = filteredItems,
            categories = categories,
            selectedCategory = selectedCat,
            onlyDairyFree = onlyDairyFree,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RecipeListUiState()
    )

    fun onCategorySelected(category: String?) {
        _selectedCategory.value = category
    }

    fun onToggleDairyFree() {
        _onlyDairyFree.value = !_onlyDairyFree.value
    }
}
