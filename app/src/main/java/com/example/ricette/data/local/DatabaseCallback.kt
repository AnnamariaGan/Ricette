package com.example.ricette.data.local
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import javax.inject.*

@Singleton
class DatabaseCallback @Inject constructor() : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val s1 = db.compileStatement("INSERT INTO food_items (id, name, kcalPer100g, carbsPer100g, proteinPer100g, fatPer100g, isDairyFree, isGlutenFree, unitLabel, gramsPerUnit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
        InitialSeedData.items.forEach { i -> s1.bindLong(1, i.id); s1.bindString(2, i.name); s1.bindDouble(3, i.kcal); s1.bindDouble(4, i.carbs); s1.bindDouble(5, i.prot); s1.bindDouble(6, i.fat); s1.bindLong(7, if (i.df) 1L else 0L); s1.bindLong(8, if (i.gf) 1L else 0L); s1.bindString(9, i.unit); s1.bindDouble(10, i.gPerUnit); s1.executeInsert(); s1.clearBindings() }
        val s2 = db.compileStatement("INSERT INTO recipes (id, title, category, baseServings, prepTimeMinutes, instructions, personalNotes) VALUES (?, ?, ?, ?, ?, ?, ?)")
        InitialSeedData.recipes.forEach { r -> s2.bindLong(1, r.id); s2.bindString(2, r.title); s2.bindString(3, r.cat); s2.bindLong(4, r.serv.toLong()); s2.bindLong(5, r.min.toLong()); s2.bindString(6, r.steps); s2.bindString(7, r.notes); s2.executeInsert(); s2.clearBindings() }
        val s3 = db.compileStatement("INSERT INTO recipe_ingredients (recipeId, foodItemId, amountInGrams) VALUES (?, ?, ?)")
        InitialSeedData.ings.forEach { ing -> s3.bindLong(1, ing.rId); s3.bindLong(2, ing.fId); s3.bindDouble(3, ing.g); s3.executeInsert(); s3.clearBindings() }
    }
}
