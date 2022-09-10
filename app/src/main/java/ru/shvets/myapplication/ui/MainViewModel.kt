package ru.shvets.myapplication.ui

import android.app.Application
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

    fun search(searchQuery: String): LiveData<List<Recipe>> {
        return recipeRepository.search(searchQuery)
    }

    fun save(recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.save(recipe)
        }
    }

    fun delete(recipe: RecipeCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.delete(recipe)
        }
    }

    fun updateLiked(recipe: RecipeCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.updateLiked(recipe)
        }
    }
//
//    fun updateDragDown(recipeStart: Recipe, recipeEnd: Recipe) {
//        viewModelScope.launch(Dispatchers.IO) {
//            recipeRepository.delete(recipeEnd)
//            recipeRepository.delete(recipeStart)
//            val newRecipeEnd = recipeStart.copy(id = recipeEnd.id)
//            recipeRepository.insert(newRecipeEnd)
//            val newRecipeStart = recipeEnd.copy(id = recipeStart.id)
//            recipeRepository.insert(newRecipeStart)
//        }
//    }
}