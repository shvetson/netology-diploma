package ru.shvets.myapplication.db.repository

import androidx.lifecycle.LiveData
import ru.shvets.myapplication.model.Recipe
import ru.shvets.myapplication.model.RecipeCategory

interface RecipeRepository {
    val getAll: LiveData<List<Recipe>>
    fun getAllRecipes(): LiveData<List<RecipeCategory>>
    fun getById(id: Long): Recipe
    suspend fun getCount(): Int
    fun getFavorites(): LiveData<List<RecipeCategory>>
    fun search(searchQuery: String): LiveData<List<Recipe>>
    suspend fun delete(recipe: RecipeCategory)
    suspend fun insert(recipe: Recipe)
    suspend fun save(recipe: Recipe)
    suspend fun updateLiked(recipe: RecipeCategory)
    suspend fun updateId(recipe: Recipe)
    suspend fun remove(recipe: Recipe, onSuccess: ()->Unit)
}