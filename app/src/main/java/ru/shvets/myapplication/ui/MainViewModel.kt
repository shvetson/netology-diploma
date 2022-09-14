package ru.shvets.myapplication.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.shvets.myapplication.App
import ru.shvets.myapplication.model.Recipe
import ru.shvets.myapplication.model.RecipeCategory
import ru.shvets.myapplication.utils.Constants

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val recipeRepository = (application.applicationContext as App).recipeRepository

    val data get() = recipeRepository.getAll

    fun getAllRecipes(): LiveData<List<RecipeCategory>> {
        return recipeRepository.getAllRecipes()
    }

    fun loadRecipes(): List<Recipe> {
        return data
    }

    fun getRecipe(id: Long) : Recipe {
        return recipeRepository.getById(id)
    }

    fun getFavorites(): LiveData<List<RecipeCategory>> {
        return recipeRepository.getFavorites()
    }

    fun search(searchQuery: String): LiveData<List<RecipeCategory>> {
        return recipeRepository.search(searchQuery)
    }

    fun save(recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.save(recipe)
        }
    }

    fun delete(recipe: RecipeCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.delete(recipe.id)
        }
    }

    fun updateLiked(recipe: RecipeCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.updateLiked(recipe)
        }
    }

    fun updateDragDrop(recipeStart: Recipe, recipeEnd: Recipe) {
        val startSortId = recipeStart.sortId
        val endSortId = recipeEnd.sortId

        Log.d(Constants.TAG, "$startSortId -> $endSortId")

        recipeRepository.updateSortId(sortId = 99999, id = recipeStart.id)
        Log.d(Constants.TAG, "$recipeStart.toString()")
        recipeRepository.updateSortId(sortId = startSortId, id = recipeEnd.id)
        Log.d(Constants.TAG, "$recipeEnd.toString()")
        recipeRepository.updateSortId(sortId = endSortId, id = recipeStart.id)
        Log.d(Constants.TAG, "$recipeStart.toString()")
    }

    fun updateRecipe(id: Long, name: String, author: String, categoryId: Long, sortId: Long) {
        recipeRepository.updateRecipe(id, name, author, categoryId, sortId)
    }
}