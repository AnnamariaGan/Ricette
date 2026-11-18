package com.example.ricette.ui.recipelist
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ricette.domain.repository.RecipeRepository
import com.example.ricette.domain.usecase.CalculateRecipeMacrosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class RecipeItemUi(val id: Long, val title: String, val category: String, val baseServings: Int, val prepTimeMinutes: Int, val kcalPerServing: Int, val isDairyFree: Boolean, val isGlutenFree: Boolean, val ingredientNames: String)
data class RecipeListUiState(val recipes: List<RecipeItemUi> = emptyList(), val categories: List<String> = emptyList(), val selectedCategory: String? = null, val searchQuery: String = "", val onlyDairyFree: Boolean = false, val isLoading: Boolean = true)

@HiltViewModel
class RecipeListViewModel @Inject constructor(private val recipeRepository: RecipeRepository, private val calculateRecipeMacrosUseCase: CalculateRecipeMacrosUseCase) : ViewModel() {
    private val _selectedCategory = MutableStateFlow<String?>(null)
    private val _searchQuery = MutableStateFlow("")
    private val _onlyDairyFree = MutableStateFlow(false)

    val uiState: StateFlow<RecipeListUiState> = combine(recipeRepository.getAllRecipesWithIngredients(), _selectedCategory, _searchQuery, _onlyDairyFree) { list, cat, query, dairy ->
        val defaults = listOf("Panificazione", "Pasticceria", "Primi Piatti", "Secondi Piatti", "Lievitati", "Dolci", "Contorni")
        val cats = (defaults + list.map { it.recipe.category }).distinct()
        val items = list.map { rw ->
            val m = calculateRecipeMacrosUseCase(rw.ingredients, rw.recipe.baseServings)
            RecipeItemUi(rw.recipe.id, rw.recipe.title, rw.recipe.category, rw.recipe.baseServings, rw.recipe.prepTimeMinutes, m.perServing.kcal.toInt(), rw.ingredients.isNotEmpty() && rw.ingredients.all { it.foodItem.isDairyFree }, rw.ingredients.isNotEmpty() && rw.ingredients.all { it.foodItem.isGlutenFree }, rw.ingredients.joinToString(", ") { it.foodItem.name })
        }
        val q = query.trim()
        val filtered = items.filter { (cat == null || it.category == cat) && (!dairy || it.isDairyFree) && (q.isEmpty() || it.title.contains(q, true) || it.ingredientNames.contains(q, true)) }
        RecipeListUiState(filtered, cats, cat, query, dairy, false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RecipeListUiState())

    fun onCategorySelected(category: String?) { _selectedCategory.value = category }
    fun onSearchQueryChange(query: String) { _searchQuery.value = query }
    fun onToggleDairyFree() { _onlyDairyFree.value = !_onlyDairyFree.value }
}
