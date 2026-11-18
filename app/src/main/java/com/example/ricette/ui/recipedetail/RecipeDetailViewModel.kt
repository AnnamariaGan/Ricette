package com.example.ricette.ui.recipedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ricette.domain.model.Recipe
import com.example.ricette.domain.model.RecipeIngredientWithFoodItem
import com.example.ricette.domain.model.RecipeMacros
import com.example.ricette.domain.model.ScaledIngredient
import com.example.ricette.domain.repository.RecipeRepository
import com.example.ricette.domain.usecase.CalculateRecipeMacrosUseCase
import com.example.ricette.domain.usecase.ScaleRecipeIngredientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.roundToInt

data class RecipeDetailUiState(
    val recipe: Recipe? = null,
    val originalIngredients: List<RecipeIngredientWithFoodItem> = emptyList(),
    val scaledIngredients: List<ScaledIngredient> = emptyList(),
    val currentServings: Int = 1,
    val isScaleByMainIngredient: Boolean = false,
    val selectedMainIngredientId: Long? = null,
    val targetIngredientWeightInput: String = "",
    val macros: RecipeMacros? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository,
    private val calculateRecipeMacrosUseCase: CalculateRecipeMacrosUseCase,
    private val scaleRecipeIngredientsUseCase: ScaleRecipeIngredientsUseCase
) : ViewModel() {

    private val recipeId: Long = checkNotNull(savedStateHandle["recipeId"]).toString().toLong()

    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    init {
        loadRecipe()
    }

    private fun loadRecipe() {
        viewModelScope.launch {
            recipeRepository.getRecipeWithIngredients(recipeId).collectLatest { recipeWithIngredients ->
                if (recipeWithIngredients != null) {
                    val recipe = recipeWithIngredients.recipe
                    val ingredients = recipeWithIngredients.ingredients
                    val initialServings = recipe.baseServings

                    // Pick default main ingredient (e.g. highest amount or first)
                    val defaultMainIngredient = ingredients.maxByOrNull { it.amountInGrams }?.foodItem?.id
                        ?: ingredients.firstOrNull()?.foodItem?.id

                    val defaultWeight = ingredients.find { it.foodItem.id == defaultMainIngredient }?.amountInGrams ?: 0.0

                    val initialScaled = scaleRecipeIngredientsUseCase.scaleByServings(
                        ingredients = ingredients,
                        baseServings = recipe.baseServings,
                        targetServings = initialServings
                    )

                    val initialMacros = calculateRecipeMacrosUseCase(
                        ingredients = initialScaled.map {
                            RecipeIngredientWithFoodItem(recipeId, it.foodItem, it.amountInGrams)
                        },
                        servings = initialServings
                    )

                    _uiState.update {
                        it.copy(
                            recipe = recipe,
                            originalIngredients = ingredients,
                            scaledIngredients = initialScaled,
                            currentServings = initialServings,
                            selectedMainIngredientId = defaultMainIngredient,
                            targetIngredientWeightInput = if (defaultWeight > 0) defaultWeight.toInt().toString() else "",
                            macros = initialMacros,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun incrementServings() {
        val newServings = _uiState.value.currentServings + 1
        setServings(newServings)
    }

    fun decrementServings() {
        val newServings = max(1, _uiState.value.currentServings - 1)
        setServings(newServings)
    }

    fun setServings(servings: Int) {
        val state = _uiState.value
        val recipe = state.recipe ?: return
        val safeServings = max(1, servings)

        val scaled = scaleRecipeIngredientsUseCase.scaleByServings(
            ingredients = state.originalIngredients,
            baseServings = recipe.baseServings,
            targetServings = safeServings
        )

        val macros = calculateRecipeMacrosUseCase(
            ingredients = scaled.map {
                RecipeIngredientWithFoodItem(recipeId, it.foodItem, it.amountInGrams)
            },
            servings = safeServings
        )

        _uiState.update {
            it.copy(
                currentServings = safeServings,
                scaledIngredients = scaled,
                macros = macros
            )
        }
    }

    fun toggleScalingMode(isScaleByMainIngredient: Boolean) {
        _uiState.update { it.copy(isScaleByMainIngredient = isScaleByMainIngredient) }
        if (!isScaleByMainIngredient) {
            setServings(_uiState.value.currentServings)
        } else {
            recalculateByMainIngredient()
        }
    }

    fun selectMainIngredient(foodItemId: Long) {
        val ingredient = _uiState.value.originalIngredients.find { it.foodItem.id == foodItemId }
        val currentGrams = ingredient?.amountInGrams ?: 0.0

        _uiState.update {
            it.copy(
                selectedMainIngredientId = foodItemId,
                targetIngredientWeightInput = if (currentGrams > 0) currentGrams.toInt().toString() else ""
            )
        }
        recalculateByMainIngredient()
    }

    fun updateTargetIngredientWeight(input: String) {
        _uiState.update { it.copy(targetIngredientWeightInput = input) }
        recalculateByMainIngredient()
    }

    private fun recalculateByMainIngredient() {
        val state = _uiState.value
        val recipe = state.recipe ?: return
        val selectedId = state.selectedMainIngredientId ?: return
        val targetWeight = state.targetIngredientWeightInput.toDoubleOrNull() ?: 0.0

        val baseIngredient = state.originalIngredients.find { it.foodItem.id == selectedId }
        val baseWeight = baseIngredient?.amountInGrams ?: 0.0

        if (targetWeight > 0.0 && baseWeight > 0.0) {
            val scaled = scaleRecipeIngredientsUseCase.scaleByIngredientWeight(
                ingredients = state.originalIngredients,
                baseIngredientFoodItemId = selectedId,
                targetWeightInGrams = targetWeight
            )

            val ratio = targetWeight / baseWeight
            val equivalentServings = max(1, (recipe.baseServings * ratio).roundToInt())

            val macros = calculateRecipeMacrosUseCase(
                ingredients = scaled.map {
                    RecipeIngredientWithFoodItem(recipeId, it.foodItem, it.amountInGrams)
                },
                servings = equivalentServings
            )

            _uiState.update {
                it.copy(
                    scaledIngredients = scaled,
                    macros = macros,
                    currentServings = equivalentServings
                )
            }
        }
    }
}
