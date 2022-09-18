package ru.shvets.myapplication.db.repository

import androidx.lifecycle.LiveData
import ru.shvets.myapplication.model.Recipe
import ru.shvets.myapplication.model.RecipeCategory

interface RecipeRepository {
//    val getAll: LiveData<List<Recipe>>
    val getAll: List<Recipe>
    fun getById(id: Long): Recipe
    fun getAllRecipes(): LiveData<List<RecipeCategory>>
    fun getFavorites(): LiveData<List<RecipeCategory>>
    fun search(searchQuery: String): LiveData<List<RecipeCategory>>
    fun getMaxSortId(): Int
    fun delete(id: Long)
    fun insert(recipe: Recipe): Long
    fun save(recipe: Recipe): Long
    fun updateLiked(recipe: RecipeCategory)
    fun updateSortId(sortId: Long, id: Long)
    fun remove(recipe: Recipe, onSuccess: ()->Unit)
    fun updateRecipe(id: Long, name: String, author: String, categoryId: Long, sortId: Long, preparation: Int, total: Int, portion: Int, ingredients: String)
}