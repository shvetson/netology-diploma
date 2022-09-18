package ru.shvets.myapplication.ui.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.shvets.myapplication.App
import ru.shvets.myapplication.model.Step
import java.util.*

class RecipeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val categoryRepository = (application.applicationContext as App).categoryRepository
    private val stepRepository = (application.applicationContext as App).stepRepository
    private val recipeRepository = (application.applicationContext as App).recipeRepository

    val data get() = categoryRepository.getAll
    val categories get() = categoryRepository.getCategories()

    private val _steps = MutableLiveData<List<Step>>()

    fun steps(id: Long): LiveData<List<Step>> {
        _steps.value = stepRepository.getAllSteps(id)
        return _steps
    }

    fun getMaxSortId(): Int{
        return recipeRepository.getMaxSortId()
    }

//    fun move(list: ArrayList<Step>, step: Step, moveBy: Int) {
//        val oldIndex = list.indexOfFirst { it.id == step.id }
//        if (oldIndex == -1) return
//        val newIndex = oldIndex + moveBy
//        if (newIndex < 0 || newIndex >= list.size) return
//        Collections.swap(list, oldIndex, newIndex)
//    }
}