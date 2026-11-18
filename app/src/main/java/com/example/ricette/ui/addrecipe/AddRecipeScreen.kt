package com.example.ricette.ui.addrecipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ricette.domain.model.FoodItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddRecipeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.saveSuccessEvent.collect {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuova Ricetta", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Indietro"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.saveRecipe() },
                        enabled = !uiState.isSaving
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Salva Ricetta"
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Error message banner
            if (uiState.errorMessage != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = uiState.errorMessage!!,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Recipe General Info Card
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Informazioni Principali",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = uiState.title,
                        onValueChange = { viewModel.onTitleChange(it) },
                        label = { Text("Titolo Ricetta *") },
                        placeholder = { Text("es. Pizza Margherita ad Alta Idratazione") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // Category dropdown
                    CategoryDropdown(
                        category = uiState.category,
                        onCategoryChange = { viewModel.onCategoryChange(it) }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.baseServingsInput,
                            onValueChange = { viewModel.onBaseServingsChange(it) },
                            label = { Text("Porzioni Base") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = uiState.prepTimeInput,
                            onValueChange = { viewModel.onPrepTimeChange(it) },
                            label = { Text("Tempo (min)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                }
            }

            // Ingredients Section Card
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ingredienti (${uiState.selectedIngredients.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (uiState.selectedIngredients.isEmpty()) {
                        Text(
                            text = "Nessun ingrediente ancora aggiunto.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        uiState.selectedIngredients.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.foodItem.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "${item.foodItem.kcalPer100g.toInt()} kcal/100g",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                OutlinedTextField(
                                    value = item.amountInGramsInput,
                                    onValueChange = { viewModel.onIngredientGramsChange(item.foodItem.id, it) },
                                    label = { Text("Grammi") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    suffix = { Text("g") },
                                    modifier = Modifier.width(110.dp),
                                    singleLine = true
                                )

                                IconButton(onClick = { viewModel.removeIngredient(item.foodItem.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Rimuovi ingrediente",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { viewModel.openIngredientPicker() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Text("Scegli Esistente")
                        }

                        OutlinedButton(
                            onClick = { viewModel.openCreateFoodItemDialog() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+ Nuovo Alimento")
                        }
                    }
                }
            }

            // Instructions & Notes Card
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Procedimento e Note",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = uiState.instructions,
                        onValueChange = { viewModel.onInstructionsChange(it) },
                        label = { Text("Procedimento dettagliato") },
                        placeholder = { Text("1. Impastare...\n2. Far lievitare...\n3. Infornare...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4
                    )

                    OutlinedTextField(
                        value = uiState.personalNotes,
                        onValueChange = { viewModel.onPersonalNotesChange(it) },
                        label = { Text("Note personali o consigli") },
                        placeholder = { Text("es. Utilizzare pietra refrattaria per una crosta croccante") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            }

            // Save button
            Button(
                onClick = { viewModel.saveRecipe() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !uiState.isSaving
            ) {
                Text("Salva Ricetta", style = MaterialTheme.typography.titleMedium)
            }
        }
    }

    // Dialog: Select Existing FoodItem
    if (uiState.isIngredientPickerOpen) {
        SelectFoodItemDialog(
            foodItems = uiState.availableFoodItems,
            onSelect = { viewModel.addIngredient(it) },
            onDismiss = { viewModel.closeIngredientPicker() }
        )
    }

    // Dialog: Create New FoodItem
    if (uiState.isCreateFoodItemDialogOpen) {
        CreateCustomFoodItemDialog(
            name = uiState.newFoodItemName,
            kcal = uiState.newFoodItemKcal,
            carbs = uiState.newFoodItemCarbs,
            protein = uiState.newFoodItemProtein,
            fat = uiState.newFoodItemFat,
            isDairyFree = uiState.newFoodItemIsDairyFree,
            isGlutenFree = uiState.newFoodItemIsGlutenFree,
            onFieldChange = { name, kcal, carbs, protein, fat, dairy, gluten ->
                viewModel.onNewFoodItemFieldChange(name, kcal, carbs, protein, fat, dairy, gluten)
            },
            onSave = { viewModel.saveCustomFoodItem() },
            onDismiss = { viewModel.closeCreateFoodItemDialog() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdown(
    category: String,
    onCategoryChange: (String) -> Unit
) {
    val categories = listOf("Panificazione", "Primi Piatti", "Secondi Piatti", "Lievitati", "Dolci", "Contorni", "Altro")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = category,
            onValueChange = onCategoryChange,
            label = { Text("Categoria") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { cat ->
                DropdownMenuItem(
                    text = { Text(cat) },
                    onClick = {
                        onCategoryChange(cat)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SelectFoodItemDialog(
    foodItems: List<FoodItem>,
    onSelect: (FoodItem) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filtered = remember(foodItems, searchQuery) {
        if (searchQuery.isBlank()) foodItems
        else foodItems.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleziona Ingrediente") },
        text = {
            Column(modifier = Modifier.height(350.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Cerca alimento") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filtered, key = { it.id }) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(item) }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(item.name, fontWeight = FontWeight.Medium)
                                Text(
                                    "${item.kcalPer100g.toInt()} kcal | C:${item.carbsPer100g} P:${item.proteinPer100g} F:${item.fatPer100g}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Chiudi")
            }
        }
    )
}

@Composable
private fun CreateCustomFoodItemDialog(
    name: String,
    kcal: String,
    carbs: String,
    protein: String,
    fat: String,
    isDairyFree: Boolean,
    isGlutenFree: Boolean,
    onFieldChange: (String?, String?, String?, String?, String?, Boolean?, Boolean?) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crea Nuovo Ingrediente") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { onFieldChange(it, null, null, null, null, null, null) },
                    label = { Text("Nome Ingrediente *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = kcal,
                    onValueChange = { onFieldChange(null, it, null, null, null, null, null) },
                    label = { Text("Kcal per 100g") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { onFieldChange(null, null, it, null, null, null, null) },
                        label = { Text("Carb. (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = protein,
                        onValueChange = { onFieldChange(null, null, null, it, null, null, null) },
                        label = { Text("Prot. (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = fat,
                        onValueChange = { onFieldChange(null, null, null, null, it, null, null) },
                        label = { Text("Grassi (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isDairyFree,
                        onCheckedChange = { onFieldChange(null, null, null, null, null, it, null) }
                    )
                    Text("Senza Latticini (Dairy-Free)")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isGlutenFree,
                        onCheckedChange = { onFieldChange(null, null, null, null, null, null, it) }
                    )
                    Text("Senza Glutine (Gluten-Free)")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onSave,
                enabled = name.isNotBlank()
            ) {
                Text("Crea e Aggiungi")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annulla")
            }
        }
    )
}
