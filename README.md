# Ricette

A native Android application built with **Kotlin** and **Jetpack Compose** to digitize, organize, and manage a personal collection of homemade Italian and Sicilian recipes.

> **Work in Progress:** Core local storage and initial recipe catalog are live. Nutritional tracking and external food database integrations are currently under active development.

---

## Current Features

* **Personal Recipe Catalog:** Pre-loaded with 14 authentic, tested homemade recipes digitized from a physical notebook.
* **Offline-First Architecture:** Full local persistence powered by **Room Database** so recipes are always accessible in the kitchen without an internet connection.
* **Modern UI:** Built 100% in **Jetpack Compose** following **Material Design 3** guidelines.
* **Clean Dependency Injection:** Modular architecture structured with **Dagger Hilt**.

---

## Tech Stack

* **Language:** Kotlin
* **UI Toolkit:** Jetpack Compose & Material 3
* **Local Persistence:** Room (SQLite)
* **Dependency Injection:** Hilt
* **Architecture:** MVVM (Model-View-ViewModel) & Repository Pattern
* **IDE:** Android Studio

---

## Roadmap & Planned Features

- [x] Project setup with Jetpack Compose, Room, and Hilt
- [x] Migration of 14 personal recipes into the local database
- [ ] **Macro & Calorie Breakdown:** Automatic calculation of nutritional values per serving
- [ ] **Open Food Database Integration:** Search ingredients and scan barcodes via Open Food Facts API
- [ ] **Search & Filtering:** Filter recipes by category, preparation time, or dietary constraints (e.g., dairy-free)
- [ ] **Interactive Cooking Mode:** Step-by-step view with built-in timers and screen wake-lock

---
