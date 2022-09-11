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

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val recipeRepository = (application.applicationContext as App).recipeRepository

    val data get() = recipeRepository.getAll

    fun getAllRecipes(): LiveData<List<RecipeCategory>> {
        return recipeRepository.getAllRecipes()
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

    fun updateDragDown(recipeStart: RecipeCategory, recipeEnd: RecipeCategory) {
            val recipeStartId = recipeStart.id
            val recipeEndId = recipeEnd.id

            recipeRepository.delete(recipeEndId)
            recipeRepository.delete(recipeStartId)

            val newRecipeEnd = recipeRepository.getById(recipeEndId)
            Log.d("App_tag", newRecipeEnd.toString())
            recipeRepository.insert(newRecipeEnd.copy(id = recipeEndId))

            val newRecipeStart = recipeRepository.getById(recipeStartId)
            Log.d("App_tag", newRecipeStart.toString())
            recipeRepository.insert(newRecipeStart.copy(id = recipeStartId))
    }
}