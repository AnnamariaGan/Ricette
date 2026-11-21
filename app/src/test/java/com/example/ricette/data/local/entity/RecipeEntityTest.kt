package com.example.ricette.data.local.entity

import com.example.ricette.domain.model.Recipe
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RecipeEntityTest {

    @Test
    fun `default isFavorite is false in Recipe and RecipeEntity`() {
        val domain = Recipe(
            id = 1,
            title = "Pizza",
            category = "Panificazione",
            baseServings = 4,
            prepTimeMinutes = 60,
            instructions = "Impastare e infornare",
            personalNotes = ""
        )
        assertFalse(domain.isFavorite)

        val entity = RecipeEntity.fromDomain(domain)
        assertFalse(entity.isFavorite)

        val backToDomain = entity.toDomain()
        assertFalse(backToDomain.isFavorite)
    }

    @Test
    fun `isFavorite preserves true value across domain and entity mapping`() {
        val domain = Recipe(
            id = 2,
            title = "Torta al Cacao",
            category = "Pasticceria",
            baseServings = 8,
            prepTimeMinutes = 50,
            instructions = "Mescolare e cuocere",
            personalNotes = "Buona",
            isFavorite = true
        )
        assertTrue(domain.isFavorite)

        val entity = RecipeEntity.fromDomain(domain)
        assertTrue(entity.isFavorite)

        val backToDomain = entity.toDomain()
        assertTrue(backToDomain.isFavorite)
        assertEquals(domain, backToDomain)
    }

    @Test
    fun `modified recipe updates all fields and retains id and favorite status`() {
        val original = Recipe(
            id = 5,
            title = "Pizza Margherita",
            category = "Panificazione",
            baseServings = 2,
            prepTimeMinutes = 30,
            instructions = "Stendere e infornare",
            personalNotes = "Forno caldissimo",
            isFavorite = true
        )

        val modified = original.copy(
            title = "Pizza Margherita Gourmet",
            category = "Lievitati",
            baseServings = 4,
            prepTimeMinutes = 45,
            instructions = "1. Impasto a freddo\n2. Infornare su pietra",
            personalNotes = "Aggiungere basilico fresco all'uscita"
        )

        val entity = RecipeEntity.fromDomain(modified)
        assertEquals(5L, entity.id)
        assertEquals("Pizza Margherita Gourmet", entity.title)
        assertEquals("Lievitati", entity.category)
        assertEquals(4, entity.baseServings)
        assertEquals(45, entity.prepTimeMinutes)
        assertEquals("1. Impasto a freddo\n2. Infornare su pietra", entity.instructions)
        assertEquals("Aggiungere basilico fresco all'uscita", entity.personalNotes)
        assertTrue(entity.isFavorite)

        val backToDomain = entity.toDomain()
        assertEquals(modified, backToDomain)
    }
}
