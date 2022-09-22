package ru.shvets.myapplication.ui.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.shvets.myapplication.App
import ru.shvets.myapplication.model.Step
import ru.shvets.myapplication.utils.Constants
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

    fun getMaxSortId(): Int {
        return recipeRepository.getMaxSortId()
    }

    fun move(list: ArrayList<Step>, step: Step, moveBy: Int) {
        val oldIndex = list.indexOfFirst { it.orderId == step.orderId }
        if (oldIndex == -1) return
        val newIndex = oldIndex + moveBy
        if (newIndex < 0 || newIndex >= list.size) return
        Collections.swap(list, oldIndex, newIndex)
    }

    fun delete(list: ArrayList<Step>, step: Step) {
        val position = list.indexOfFirst { it.orderId == step.orderId }
        list.removeAt(position)
    }

    fun update(list: ArrayList<Step>, step: Step, newName: String) {
        val curIndex = list.indexOfFirst { it.orderId == step.orderId }
        if (curIndex == -1) return
        val updatedStep = step.copy(name = newName)
        list[curIndex] = updatedStep
    }

    fun add(list: ArrayList<Step>, name: String) {
        val recipe = Step(
            id = Constants.NEW_STEP_ID,
            name = name,
            recipeId = Constants.NEW_RECIPE_ID,
            orderId = (list.size + 1).toLong())
        list.add(recipe)
    }
}