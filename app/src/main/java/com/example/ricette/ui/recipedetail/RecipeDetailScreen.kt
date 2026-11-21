package com.example.ricette.ui.recipedetail

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ricette.domain.model.ScaledIngredient
import com.example.ricette.ui.components.MacroCard
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit = {},
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isCookingMode by remember { mutableStateOf(false) }
    val checkedIngredients = remember { mutableStateMapOf<Long, Boolean>() }
    val checkedSteps = remember { mutableStateMapOf<Int, Boolean>() }
    val ctx = LocalContext.current
    DisposableEffect(isCookingMode) {
        val w = (ctx as? Activity)?.window
        if (isCookingMode) w?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose { w?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.recipe?.title ?: "Dettaglio Ricetta",
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Indietro"
                        )
                    }
                },
                actions = {
                    val recipe = uiState.recipe
                    val isFav = recipe?.isFavorite == true
                    if (recipe != null) {
                        IconButton(onClick = { onEditClick(recipe.id) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Modifica Ricetta",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    IconButton(onClick = viewModel::toggleFavorite) {
                        Icon(
                            imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFav) "Rimuovi dai preferiti" else "Aggiungi ai preferiti",
                            tint = if (isFav) Color.Red else MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val recipe = uiState.recipe
            if (recipe == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ricetta non trovata")
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AssistChip(onClick = {}, label = { Text(recipe.category) })
                        FilledTonalButton(onClick = { isCookingMode = !isCookingMode }) {
                            Text(if (isCookingMode) "Esci Modalita Cucina" else "Modalita Cucina")
                        }
                    }
                    if (isCookingMode) {
                        CookingTimerCard(defaultMinutes = recipe.prepTimeMinutes.coerceAtLeast(1))
                    }

                    // Recipe Info & Servings Selector Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Tempo",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${recipe.prepTimeMinutes} min",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Porzioni (Base: ${recipe.baseServings})",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    FilledTonalIconButton(
                                        onClick = { viewModel.decrementServings() },
                                        enabled = uiState.currentServings > 1,
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Text("-", style = MaterialTheme.typography.titleLarge)
                                    }
                                    Text(
                                        text = uiState.currentServings.toString(),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                    FilledTonalIconButton(
                                        onClick = { viewModel.incrementServings() },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Text("+", style = MaterialTheme.typography.titleLarge)
                                    }
                                }
                            }
                        }
                    }

                    // Macro summary card
                    uiState.macros?.let { macros ->
                        MacroCard(macros = macros)
                    }

                    // Scaling controls section
                    ScalingControlCard(
                        isScaleByMainIngredient = uiState.isScaleByMainIngredient,
                        currentServings = uiState.currentServings,
                        onModeChange = { viewModel.toggleScalingMode(it) },
                        onIncrementServings = { viewModel.incrementServings() },
                        onDecrementServings = { viewModel.decrementServings() },
                        onQuickServingsClick = { viewModel.setServings(it) },
                        ingredients = uiState.scaledIngredients,
                        selectedIngredientId = uiState.selectedMainIngredientId,
                        targetWeightInput = uiState.targetIngredientWeightInput,
                        onIngredientSelected = { viewModel.selectMainIngredient(it) },
                        onTargetWeightChange = { viewModel.updateTargetIngredientWeight(it) }
                    )

                    val isBaking = recipe.category.equals("Panificazione", true) ||
                        recipe.category.equals("Lievitati", true) ||
                        recipe.category.equals("Dolci", true) || recipe.category.equals("Pasticceria", true)
                    IngredientsCard(
                        ingredients = uiState.scaledIngredients,
                        checkedMap = checkedIngredients,
                        currentServings = uiState.currentServings,
                        baseServings = recipe.baseServings,
                        onIncrementServings = { viewModel.incrementServings() },
                        onDecrementServings = { viewModel.decrementServings() },
                        forceGrams = isBaking,
                        onToggleCheck = { id -> checkedIngredients[id] = !(checkedIngredients[id] ?: false) }
                    )
                    InteractiveInstructionsCard(
                        instructions = recipe.instructions,
                        checkedSteps = checkedSteps,
                        onToggleStep = { idx -> checkedSteps[idx] = !(checkedSteps[idx] ?: false) }
                    )

                    // Personal notes card (if present)
                    if (recipe.personalNotes.isNotBlank()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "💡 Note Personali",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = recipe.personalNotes,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }

                    // Edit button at the bottom
                    OutlinedButton(
                        onClick = { onEditClick(recipe.id) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Modifica Ricetta", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScalingControlCard(
    isScaleByMainIngredient: Boolean,
    currentServings: Int,
    onModeChange: (Boolean) -> Unit,
    onIncrementServings: () -> Unit,
    onDecrementServings: () -> Unit,
    onQuickServingsClick: (Int) -> Unit,
    ingredients: List<ScaledIngredient>,
    selectedIngredientId: Long?,
    targetWeightInput: String,
    onIngredientSelected: (Long) -> Unit,
    onTargetWeightChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Opzioni Avanzate Scaling",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = !isScaleByMainIngredient,
                    onClick = { onModeChange(false) },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                ) {
                    Text("Per Porzioni")
                }
                SegmentedButton(
                    selected = isScaleByMainIngredient,
                    onClick = { onModeChange(true) },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                ) {
                    Text("Su Ingrediente")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isScaleByMainIngredient) {
                // Quick serving buttons
                Text(
                    text = "Selezione rapida porzioni:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(1, 2, 4, 6, 8).forEach { servings ->
                        FilledTonalButton(
                            onClick = { onQuickServingsClick(servings) },
                            modifier = Modifier.weight(1f),
                            contentPadding = ButtonDefaults.TextButtonContentPadding
                        ) {
                            Text(servings.toString())
                        }
                    }
                }
            } else {
                // Scale by main ingredient selector
                var dropdownExpanded by remember { mutableStateOf(false) }
                val selectedIngredient = ingredients.find { it.foodItem.id == selectedIngredientId }

                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedIngredient?.foodItem?.name ?: "Seleziona ingrediente",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ingrediente Guida") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    )

                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        ingredients.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item.foodItem.name) },
                                onClick = {
                                    onIngredientSelected(item.foodItem.id)
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = targetWeightInput,
                    onValueChange = onTargetWeightChange,
                    label = { Text("Peso desiderato (grammi)") },
                    placeholder = { Text("es. 600") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    suffix = { Text("g") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tutti gli altri ingredienti e i macronutrienti si ricalcolano proporzionalmente.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun IngredientsCard(
    ingredients: List<ScaledIngredient>,
    checkedMap: Map<Long, Boolean>,
    currentServings: Int,
    baseServings: Int,
    onIncrementServings: () -> Unit,
    onDecrementServings: () -> Unit,
    forceGrams: Boolean,
    onToggleCheck: (Long) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Ingredienti", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        text = if (currentServings != baseServings) "Porzioni: $currentServings (base: $baseServings)" else "Porzioni: $currentServings",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilledTonalIconButton(
                        onClick = onDecrementServings,
                        enabled = currentServings > 1,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text("-", style = MaterialTheme.typography.titleMedium)
                    }
                    Text(
                        text = currentServings.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    FilledTonalIconButton(
                        onClick = onIncrementServings,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text("+", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            ingredients.forEach { ingredient ->
                val isChecked = checkedMap[ingredient.foodItem.id] == true
                val amt = if (forceGrams || (ingredient.foodItem.unitLabel == "g")) {
                    if (ingredient.amountInGrams % 1.0 == 0.0) "${ingredient.amountInGrams.toInt()} g" else "${ingredient.amountInGrams} g"
                } else ingredient.foodItem.formatAmount(ingredient.amountInGrams)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleCheck(ingredient.foodItem.id) }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isChecked, onCheckedChange = { onToggleCheck(ingredient.foodItem.id) })
                        Text(
                            text = ingredient.foodItem.name,
                            style = MaterialTheme.typography.bodyLarge,
                            textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                        )
                    }
                    Text(
                        text = amt,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun InteractiveInstructionsCard(instructions: String, checkedSteps: Map<Int, Boolean>, onToggleStep: (Int) -> Unit) {
    val steps = remember(instructions) { instructions.lines().map { it.trim() }.filter { it.isNotEmpty() } }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Procedimento Interattivo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("${steps.indices.count { checkedSteps[it] == true }}/${steps.size}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            }
            steps.forEachIndexed { i, step ->
                val done = checkedSteps[i] == true
                Row(modifier = Modifier.fillMaxWidth().clickable { onToggleStep(i) }, verticalAlignment = Alignment.Top) {
                    Checkbox(checked = done, onCheckedChange = { onToggleStep(i) })
                    Text(step, style = MaterialTheme.typography.bodyMedium, textDecoration = if (done) TextDecoration.LineThrough else TextDecoration.None, modifier = Modifier.padding(top = 10.dp))
                }
            }
        }
    }
}

@Composable
private fun CookingTimerCard(defaultMinutes: Int) {
    var sec by remember { mutableIntStateOf(defaultMinutes * 60) }
    var run by remember { mutableStateOf(false) }
    LaunchedEffect(run, sec) {
        if (run && sec > 0) { delay(1000L); sec -= 1; if (sec == 0) run = false }
    }
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Timer Cucina (Schermo Attivo)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text("%02d:%02d".format(sec / 60, sec % 60), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { run = !run }) { Text(if (run) "Pausa" else "Avvia") }
                    OutlinedButton(onClick = { run = false; sec = defaultMinutes * 60 }) { Text("Reset") }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(-1, 1, 5, 10).forEach { d ->
                    FilledTonalButton(onClick = { sec = (sec + d * 60).coerceAtLeast(0) }, modifier = Modifier.weight(1f)) {
                        Text(if (d > 0) "+${d}m" else "${d}m")
                    }
                }
            }
        }
    }
}
