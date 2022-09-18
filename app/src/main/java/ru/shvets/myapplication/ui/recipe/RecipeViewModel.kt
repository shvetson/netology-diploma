package ru.shvets.myapplication.ui.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.shvets.myapplication.App
import ru.shvets.myapplication.model.Step

class RecipeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val categoryRepository = (application.applicationContext as App).categoryRepository
    private val stepRepository = (application.applicationContext as App).stepRepository
    private val recipeRepository = (application.applicationContext as App).recipeRepository

    val data get() = categoryRepository.getAll

    val categories get() = categoryRepository.getCategories()

    private val _list: MutableLiveData<List<Step>> by lazy {
        MutableLiveData<List<Step>>()
    }

    val list: LiveData<List<Step>> = _list

    fun loadListSteps(id: Long): List<Step> {
        return stepRepository.getAllSteps(id)
    }

    fun steps(id: Long) = stepRepository.getAll(id)

    fun getMaxSortId(): Int{
        return recipeRepository.getMaxSortId()
    }
}