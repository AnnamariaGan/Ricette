package com.example.ricette.ui.recipelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ricette.domain.repository.RecipeRepository
import com.example.ricette.domain.usecase.CalculateRecipeMacrosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeItemUi(
    val id: Long,
    val title: String,
    val category: String,
    val baseServings: Int,
    val prepTimeMinutes: Int,
    val kcalPerServing: Int,
    val isDairyFree: Boolean,
    val isGlutenFree: Boolean,
    val ingredientNames: String,
    val isFavorite: Boolean = false,
)

data class RecipeListUiState(
    val recipes: List<RecipeItemUi> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val searchQuery: String = "",
    val onlyDairyFree: Boolean = false,
    val onlyFavorites: Boolean = false,
    val isLoading: Boolean = true,
)

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val calculateRecipeMacrosUseCase: CalculateRecipeMacrosUseCase,
) : ViewModel() {
    private val _selectedCategory = MutableStateFlow<String?>(null)
    private val _searchQuery = MutableStateFlow("")
    private val _onlyDairyFree = MutableStateFlow(false)
    private val _onlyFavorites = MutableStateFlow(false)

    val uiState: StateFlow<RecipeListUiState> = combine(
        recipeRepository.getAllRecipesWithIngredients(),
        _selectedCategory,
        _searchQuery,
        _onlyDairyFree,
        _onlyFavorites,
    ) { list, cat, query, dairy, fav ->
        val defaults = listOf(
            "Primi Piatti",
            "Secondi Piatti",
            "Dolci",
            "Lievitati",
            "Panificazione",
            "Pasticceria",
            "Contorni",
        )
        val recipeCategories = list.map { it.recipe.category }.filter { it.isNotBlank() }
        val cats = (defaults + recipeCategories).distinct()

        val items = list.map { rw ->
            val m = calculateRecipeMacrosUseCase(rw.ingredients, rw.recipe.baseServings)
            RecipeItemUi(
                id = rw.recipe.id,
                title = rw.recipe.title,
                category = rw.recipe.category,
                baseServings = rw.recipe.baseServings,
                prepTimeMinutes = rw.recipe.prepTimeMinutes,
                kcalPerServing = m.perServing.kcal.toInt(),
                isDairyFree = rw.ingredients.isNotEmpty() && rw.ingredients.all { it.foodItem.isDairyFree },
                isGlutenFree = rw.ingredients.isNotEmpty() && rw.ingredients.all { it.foodItem.isGlutenFree },
                ingredientNames = rw.ingredients.joinToString(", ") { it.foodItem.name },
                isFavorite = rw.recipe.isFavorite,
            )
        }
        val q = query.trim()
        val filtered = items.filter { item ->
            val matchesCat = if (cat == null) {
                true
            } else {
                item.category.equals(cat, ignoreCase = true) ||
                    (cat.startsWith("Primi", ignoreCase = true) && item.category.startsWith("Primi", ignoreCase = true)) ||
                    (cat.startsWith("Secondi", ignoreCase = true) && item.category.startsWith("Secondi", ignoreCase = true)) ||
                    (cat.equals("Dolci", ignoreCase = true) && (item.category.contains("Dolc", ignoreCase = true) || item.category.equals("Pasticceria", ignoreCase = true))) ||
                    (cat.equals("Lievitati", ignoreCase = true) && (item.category.equals("Lievitati", ignoreCase = true) || item.category.equals("Panificazione", ignoreCase = true)))
            }
            val matchesDairy = !dairy || item.isDairyFree
            val matchesFav = !fav || item.isFavorite
            val matchesQuery = q.isEmpty() ||
                item.title.contains(q, ignoreCase = true) ||
                item.ingredientNames.contains(q, ignoreCase = true) ||
                item.category.contains(q, ignoreCase = true)

            matchesCat && matchesDairy && matchesFav && matchesQuery
        }
        RecipeListUiState(
            recipes = filtered,
            categories = cats,
            selectedCategory = cat,
            searchQuery = query,
            onlyDairyFree = dairy,
            onlyFavorites = fav,
            isLoading = false,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RecipeListUiState())

    fun onCategorySelected(category: String?) {
        _selectedCategory.value = category
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onToggleDairyFree() {
        _onlyDairyFree.value = !_onlyDairyFree.value
    }

    fun onToggleFavorites() {
        _onlyFavorites.value = !_onlyFavorites.value
    }

    fun onToggleFavorite(recipeId: Long) {
        viewModelScope.launch {
            val item = uiState.value.recipes.find { it.id == recipeId } ?: return@launch
            recipeRepository.updateFavoriteStatus(recipeId, !item.isFavorite)
        }
    }
}
