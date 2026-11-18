package com.example.ricette.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseCallback @Inject constructor() : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        prepopulateFoodItems(db)
        prepopulateRecipes(db)
        prepopulateRecipeIngredients(db)
    }

    private fun prepopulateFoodItems(db: SupportSQLiteDatabase) {
        val stmt = db.compileStatement(
            """
            INSERT INTO food_items (id, name, kcalPer100g, carbsPer100g, proteinPer100g, fatPer100g, isDairyFree, isGlutenFree)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """.trimIndent()
        )

        val items = listOf(
            ItemData(1L, "Farina 00", 343.0, 73.0, 11.0, 1.0, isDairyFree = true, isGlutenFree = false),
            ItemData(2L, "Farina Manitoba", 350.0, 70.0, 14.0, 1.5, isDairyFree = true, isGlutenFree = false),
            ItemData(3L, "Farina Integrale", 330.0, 65.0, 13.0, 2.5, isDairyFree = true, isGlutenFree = false),
            ItemData(4L, "Semola Rimacinata di Grano Duro", 345.0, 71.0, 12.5, 1.5, isDairyFree = true, isGlutenFree = false),
            ItemData(5L, "Acqua", 0.0, 0.0, 0.0, 0.0, isDairyFree = true, isGlutenFree = true),
            ItemData(6L, "Lievito di Birra Fresco", 96.0, 1.5, 12.5, 1.5, isDairyFree = true, isGlutenFree = true),
            ItemData(7L, "Olio Extravergine d'Oliva", 884.0, 0.0, 0.0, 100.0, isDairyFree = true, isGlutenFree = true),
            ItemData(8L, "Sale Fino", 0.0, 0.0, 0.0, 0.0, isDairyFree = true, isGlutenFree = true),
            ItemData(9L, "Zucchero Semolato", 392.0, 100.0, 0.0, 0.0, isDairyFree = true, isGlutenFree = true),
            ItemData(10L, "Pomodori Pelati", 21.0, 3.5, 1.2, 0.2, isDairyFree = true, isGlutenFree = true),
            ItemData(11L, "Passata di Pomodoro", 24.0, 4.0, 1.3, 0.2, isDairyFree = true, isGlutenFree = true),
            ItemData(12L, "Petto di Pollo", 110.0, 0.0, 23.0, 1.5, isDairyFree = true, isGlutenFree = true),
            ItemData(13L, "Uova Medie", 143.0, 0.8, 12.6, 9.5, isDairyFree = true, isGlutenFree = true),
            ItemData(14L, "Riso Carnaroli", 349.0, 78.0, 7.0, 0.8, isDairyFree = true, isGlutenFree = true),
            ItemData(15L, "Pasta di Semola", 353.0, 72.0, 13.0, 1.5, isDairyFree = true, isGlutenFree = false),
            ItemData(16L, "Zucchine", 17.0, 3.1, 1.2, 0.3, isDairyFree = true, isGlutenFree = true),
            ItemData(17L, "Melanzane", 25.0, 5.9, 1.0, 0.2, isDairyFree = true, isGlutenFree = true),
            ItemData(18L, "Cipolla Dorata", 40.0, 9.3, 1.1, 0.1, isDairyFree = true, isGlutenFree = true),
            ItemData(19L, "Aglio", 149.0, 33.0, 6.4, 0.5, isDairyFree = true, isGlutenFree = true),
            ItemData(20L, "Basilico Fresco", 23.0, 2.7, 3.2, 0.6, isDairyFree = true, isGlutenFree = true),
            ItemData(21L, "Ceci Lessati", 120.0, 17.0, 7.0, 2.5, isDairyFree = true, isGlutenFree = true),
            ItemData(22L, "Tonno al Naturale", 103.0, 0.0, 24.0, 0.8, isDairyFree = true, isGlutenFree = true),
            ItemData(23L, "Parmigiano Reggiano DOP", 402.0, 0.0, 33.0, 30.0, isDairyFree = false, isGlutenFree = true),
            ItemData(24L, "Mozzarella Fiordilatte", 250.0, 1.0, 18.0, 19.5, isDairyFree = false, isGlutenFree = true),
            ItemData(25L, "Latte Intero", 64.0, 4.8, 3.3, 3.6, isDairyFree = false, isGlutenFree = true),
            ItemData(26L, "Burro", 717.0, 0.1, 0.9, 81.0, isDairyFree = false, isGlutenFree = true),
            ItemData(27L, "Cioccolato Fondente 70%", 546.0, 35.0, 7.8, 42.0, isDairyFree = true, isGlutenFree = true),
            ItemData(28L, "Mandorle", 579.0, 22.0, 21.0, 50.0, isDairyFree = true, isGlutenFree = true)
        )

        for (item in items) {
            stmt.bindLong(1, item.id)
            stmt.bindString(2, item.name)
            stmt.bindDouble(3, item.kcalPer100g)
            stmt.bindDouble(4, item.carbsPer100g)
            stmt.bindDouble(5, item.proteinPer100g)
            stmt.bindDouble(6, item.fatPer100g)
            stmt.bindLong(7, if (item.isDairyFree) 1L else 0L)
            stmt.bindLong(8, if (item.isGlutenFree) 1L else 0L)
            stmt.executeInsert()
            stmt.clearBindings()
        }
    }

    private fun prepopulateRecipes(db: SupportSQLiteDatabase) {
        val stmt = db.compileStatement(
            """
            INSERT INTO recipes (id, title, category, baseServings, prepTimeMinutes, instructions, personalNotes)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """.trimIndent()
        )

        val recipes = listOf(
            RecipeData(
                id = 1L,
                title = "Impasto Pane Bianco Veloce",
                category = "Panificazione",
                baseServings = 4,
                prepTimeMinutes = 90,
                instructions = "1. Sciogliere il lievito nell'acqua tiepida.\n2. In una ciotola capiente unire la farina 00 e la farina manitoba.\n3. Aggiungere l'acqua con il lievito e iniziare a mescolare.\n4. Incorporare il sale e l'olio EVO, impastando per 10 minuti fino a ottenere un panetto liscio.\n5. Far lievitare per circa 1 ora a temperatura ambiente coperto.\n6. Dare forma alla pagnotta e cuocere in forno statico a 220°C per 30-35 minuti fino a doratura.",
                personalNotes = "Idratazione al 65%. Ideale per pane croccante all'esterno e soffice all'interno."
            ),
            RecipeData(
                id = 2L,
                title = "Pasta al Pomodoro e Olio EVO",
                category = "Primi Piatti",
                baseServings = 2,
                prepTimeMinutes = 20,
                instructions = "1. Mettere a bollire abbondante acqua salata in una pentola.\n2. In una padella capiente far scaldare l'olio EVO con lo spicchio d'aglio a fuoco dolce.\n3. Aggiungere i pomodori pelati schiacciati con una forchetta e un pizzico di sale.\n4. Lasciar sobbollire il sugo per circa 15 minuti mescolando di tanto in tanto.\n5. Cuocere la pasta di semola e scolarla al dente tenendo da parte un goccio di acqua di cottura.\n6. Saltare la pasta nella padella col sugo per un minuto, guarnire con basilico fresco e Parmigiano Reggiano grattugiato.",
                personalNotes = "Il grande classico mediterraneo, semplice, profumato e bilanciato."
            )
        )

        for (recipe in recipes) {
            stmt.bindLong(1, recipe.id)
            stmt.bindString(2, recipe.title)
            stmt.bindString(3, recipe.category)
            stmt.bindLong(4, recipe.baseServings.toLong())
            stmt.bindLong(5, recipe.prepTimeMinutes.toLong())
            stmt.bindString(6, recipe.instructions)
            stmt.bindString(7, recipe.personalNotes)
            stmt.executeInsert()
            stmt.clearBindings()
        }
    }

    private fun prepopulateRecipeIngredients(db: SupportSQLiteDatabase) {
        val stmt = db.compileStatement(
            """
            INSERT INTO recipe_ingredients (recipeId, foodItemId, amountInGrams)
            VALUES (?, ?, ?)
            """.trimIndent()
        )

        val ingredients = listOf(
            // Ricetta 1: Impasto Pane Bianco Veloce
            IngredientData(1L, 1L, 300.0), // Farina 00
            IngredientData(1L, 2L, 200.0), // Farina Manitoba
            IngredientData(1L, 5L, 325.0), // Acqua
            IngredientData(1L, 6L, 10.0),  // Lievito di birra fresco
            IngredientData(1L, 8L, 10.0),  // Sale fino
            IngredientData(1L, 7L, 15.0),  // Olio EVO

            // Ricetta 2: Pasta al Pomodoro e Olio EVO
            IngredientData(2L, 15L, 180.0), // Pasta di semola
            IngredientData(2L, 10L, 250.0), // Pomodori pelati
            IngredientData(2L, 7L, 20.0),   // Olio EVO
            IngredientData(2L, 19L, 5.0),   // Aglio
            IngredientData(2L, 20L, 5.0),   // Basilico fresco
            IngredientData(2L, 8L, 4.0),    // Sale fino
            IngredientData(2L, 23L, 20.0)   // Parmigiano Reggiano
        )

        for (ing in ingredients) {
            stmt.bindLong(1, ing.recipeId)
            stmt.bindLong(2, ing.foodItemId)
            stmt.bindDouble(3, ing.amountInGrams)
            stmt.executeInsert()
            stmt.clearBindings()
        }
    }

    private data class ItemData(
        val id: Long,
        val name: String,
        val kcalPer100g: Double,
        val carbsPer100g: Double,
        val proteinPer100g: Double,
        val fatPer100g: Double,
        val isDairyFree: Boolean,
        val isGlutenFree: Boolean
    )

    private data class RecipeData(
        val id: Long,
        val title: String,
        val category: String,
        val baseServings: Int,
        val prepTimeMinutes: Int,
        val instructions: String,
        val personalNotes: String
    )

    private data class IngredientData(
        val recipeId: Long,
        val foodItemId: Long,
        val amountInGrams: Double
    )
}
