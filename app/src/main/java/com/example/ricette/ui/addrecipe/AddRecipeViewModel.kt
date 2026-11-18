package com.example.ricette.ui.addrecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ricette.domain.model.FoodItem
import com.example.ricette.domain.model.Recipe
import com.example.ricette.domain.model.RecipeIngredient
import com.example.ricette.domain.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeIngredientDraft(
    val foodItem: FoodItem,
    val amountInGramsInput: String = "100"
)

data class AddRecipeUiState(
    val title: String = "",
    val category: String = "Panificazione",
    val baseServingsInput: String = "4",
    val prepTimeInput: String = "30",
    val instructions: String = "",
    val personalNotes: String = "",
    val selectedIngredients: List<RecipeIngredientDraft> = emptyList(),
    val availableFoodItems: List<FoodItem> = emptyList(),
    val isIngredientPickerOpen: Boolean = false,
    val isCreateFoodItemDialogOpen: Boolean = false,
    val newFoodItemName: String = "",
    val newFoodItemKcal: String = "",
    val newFoodItemCarbs: String = "",
    val newFoodItemProtein: String = "",
    val newFoodItemFat: String = "",
    val newFoodItemIsDairyFree: Boolean = true,
    val newFoodItemIsGlutenFree: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AddRecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddRecipeUiState())
    val uiState: StateFlow<AddRecipeUiState> = _uiState.asStateFlow()

    private val _saveSuccessEvent = MutableSharedFlow<Unit>()
    val saveSuccessEvent: SharedFlow<Unit> = _saveSuccessEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            recipeRepository.getAllFoodItems().collectLatest { items ->
                _uiState.update { it.copy(availableFoodItems = items) }
            }
        }
    }

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title, errorMessage = null) }
    }

    fun onCategoryChange(category: String) {
        _uiState.update { it.copy(category = category) }
    }

    fun onBaseServingsChange(servings: String) {
        _uiState.update { it.copy(baseServingsInput = servings) }
    }

    fun onPrepTimeChange(prepTime: String) {
        _uiState.update { it.copy(prepTimeInput = prepTime) }
    }

    fun onInstructionsChange(instructions: String) {
        _uiState.update { it.copy(instructions = instructions) }
    }

    fun onPersonalNotesChange(notes: String) {
        _uiState.update { it.copy(personalNotes = notes) }
    }

    fun openIngredientPicker() {
        _uiState.update { it.copy(isIngredientPickerOpen = true) }
    }

    fun closeIngredientPicker() {
        _uiState.update { it.copy(isIngredientPickerOpen = false) }
    }

    fun addIngredient(foodItem: FoodItem) {
        val current = _uiState.value.selectedIngredients
        if (current.none { it.foodItem.id == foodItem.id }) {
            _uiState.update {
                it.copy(
                    selectedIngredients = current + RecipeIngredientDraft(foodItem = foodItem),
                    isIngredientPickerOpen = false
                )
            }
        } else {
            _uiState.update { it.copy(isIngredientPickerOpen = false) }
        }
    }

    fun removeIngredient(foodItemId: Long) {
        _uiState.update {
            it.copy(selectedIngredients = it.selectedIngredients.filterNot { item -> item.foodItem.id == foodItemId })
        }
    }

    fun onIngredientGramsChange(foodItemId: Long, grams: String) {
        _uiState.update { state ->
            state.copy(
                selectedIngredients = state.selectedIngredients.map { item ->
                    if (item.foodItem.id == foodItemId) {
                        item.copy(amountInGramsInput = grams)
                    } else {
                        item
                    }
                }
            )
        }
    }

    // Custom FoodItem dialog
    fun openCreateFoodItemDialog() {
        _uiState.update {
            it.copy(
                isCreateFoodItemDialogOpen = true,
                newFoodItemName = "",
                newFoodItemKcal = "",
                newFoodItemCarbs = "",
                newFoodItemProtein = "",
                newFoodItemFat = "",
                newFoodItemIsDairyFree = true,
                newFoodItemIsGlutenFree = true
            )
        }
    }

    fun closeCreateFoodItemDialog() {
        _uiState.update { it.copy(isCreateFoodItemDialogOpen = false) }
    }

    fun onNewFoodItemFieldChange(
        name: String? = null,
        kcal: String? = null,
        carbs: String? = null,
        protein: String? = null,
        fat: String? = null,
        isDairyFree: Boolean? = null,
        isGlutenFree: Boolean? = null
    ) {
        _uiState.update {
            it.copy(
                newFoodItemName = name ?: it.newFoodItemName,
                newFoodItemKcal = kcal ?: it.newFoodItemKcal,
                newFoodItemCarbs = carbs ?: it.newFoodItemCarbs,
                newFoodItemProtein = protein ?: it.newFoodItemProtein,
                newFoodItemFat = fat ?: it.newFoodItemFat,
                newFoodItemIsDairyFree = isDairyFree ?: it.newFoodItemIsDairyFree,
                newFoodItemIsGlutenFree = isGlutenFree ?: it.newFoodItemIsGlutenFree
            )
        }
    }

    fun saveCustomFoodItem() {
        val state = _uiState.value
        val name = state.newFoodItemName.trim()
        if (name.isBlank()) return

        val kcal = state.newFoodItemKcal.toDoubleOrNull() ?: 0.0
        val carbs = state.newFoodItemCarbs.toDoubleOrNull() ?: 0.0
        val protein = state.newFoodItemProtein.toDoubleOrNull() ?: 0.0
        val fat = state.newFoodItemFat.toDoubleOrNull() ?: 0.0

        viewModelScope.launch {
            val customFoodItem = FoodItem(
                name = name,
                kcalPer100g = kcal,
                carbsPer100g = carbs,
                proteinPer100g = protein,
                fatPer100g = fat,
                isDairyFree = state.newFoodItemIsDairyFree,
                isGlutenFree = state.newFoodItemIsGlutenFree
            )
            val newId = recipeRepository.insertFoodItem(customFoodItem)
            val savedItem = customFoodItem.copy(id = newId)

            _uiState.update {
                it.copy(
                    isCreateFoodItemDialogOpen = false,
                    selectedIngredients = it.selectedIngredients + RecipeIngredientDraft(foodItem = savedItem)
                )
            }
        }
    }

    fun saveRecipe() {
        val state = _uiState.value
        val title = state.title.trim()
        if (title.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Inserisci un titolo per la ricetta") }
            return
        }

        val baseServings = state.baseServingsInput.toIntOrNull() ?: 1
        val prepTime = state.prepTimeInput.toIntOrNull() ?: 0

        val validIngredients = state.selectedIngredients.mapNotNull { item ->
            val grams = item.amountInGramsInput.toDoubleOrNull()
            if (grams != null && grams > 0) {
                item.foodItem.id to grams
            } else null
        }

        if (validIngredients.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Aggiungi almeno un ingrediente con quantità valida") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val recipe = Recipe(
                title = title,
                category = state.category.ifBlank { "Altro" },
                baseServings = baseServings,
                prepTimeMinutes = prepTime,
                instructions = state.instructions.trim(),
                personalNotes = state.personalNotes.trim()
            )

            val recipeId = recipeRepository.insertRecipe(recipe)

            validIngredients.forEach { (foodItemId, grams) ->
                recipeRepository.addIngredientToRecipe(
                    RecipeIngredient(
                        recipeId = recipeId,
                        foodItemId = foodItemId,
                        amountInGrams = grams
                    )
                )
            }

            _saveSuccessEvent.emit(Unit)
        }
    }
}
