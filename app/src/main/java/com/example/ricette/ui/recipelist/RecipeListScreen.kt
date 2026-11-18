package com.example.ricette.ui.recipelist
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(onRecipeClick: (Long) -> Unit, onAddRecipeClick: () -> Unit, viewModel: RecipeListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { TopAppBar(title = { Column { Text("Il Mio Ricettario", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("${uiState.recipes.size} ricette disponibili", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)) },
        floatingActionButton = { ExtendedFloatingActionButton(onClick = onAddRecipeClick, icon = { Icon(Icons.Default.Add, null) }, text = { Text("Nuova Ricetta", fontWeight = FontWeight.SemiBold) }, containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary) }
    ) { pv ->
        Column(Modifier.fillMaxSize().padding(pv)) {
            OutlinedTextField(value = uiState.searchQuery, onValueChange = viewModel::onSearchQueryChange, placeholder = { Text("Cerca ricetta o ingrediente...") }, leadingIcon = { Icon(Icons.Default.Search, null) }, trailingIcon = { if (uiState.searchQuery.isNotEmpty()) IconButton(onClick = { viewModel.onSearchQueryChange("") }) { Icon(Icons.Default.Clear, "Cancella") } }, shape = RoundedCornerShape(16.dp), singleLine = true, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp))
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { FilterChip(selected = uiState.selectedCategory == null, onClick = { viewModel.onCategorySelected(null) }, label = { Text("Tutte") }) }
                items(uiState.categories) { cat -> FilterChip(selected = uiState.selectedCategory == cat, onClick = { viewModel.onCategorySelected(if (uiState.selectedCategory == cat) null else cat) }, label = { Text(cat) }) }
                item { FilterChip(selected = uiState.onlyDairyFree, onClick = viewModel::onToggleDairyFree, label = { Text("Senza Latticini") }) }
            }
            if (uiState.isLoading) Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            else if (uiState.recipes.isEmpty()) Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) { Text("Nessuna ricetta trovata.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            else LazyColumn(contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(uiState.recipes, key = { it.id }) { recipe ->
                    Card(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).clickable { onRecipeClick(recipe.id) }, shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)) {
                        Column(Modifier.padding(16.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primaryContainer) { Text(recipe.category.uppercase(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) }
                                if (recipe.isDairyFree) Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.secondaryContainer) { Text("Senza Latticini", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) }
                            }
                            Spacer(Modifier.height(10.dp))
                            Text(recipe.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            if (recipe.ingredientNames.isNotEmpty()) { Spacer(Modifier.height(4.dp)); Text(recipe.ingredientNames, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                            Spacer(Modifier.height(12.dp))
                            Row(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f), RoundedCornerShape(12.dp)).padding(horizontal = 12.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("${recipe.prepTimeMinutes} min", style = MaterialTheme.typography.labelMedium)
                                Text("${recipe.baseServings} porzioni", style = MaterialTheme.typography.labelMedium)
                                Text("${recipe.kcalPerServing} kcal/porz.", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}
