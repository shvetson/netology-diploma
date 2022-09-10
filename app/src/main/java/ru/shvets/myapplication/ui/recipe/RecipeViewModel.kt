package ru.shvets.myapplication.ui.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.shvets.myapplication.App
import ru.shvets.myapplication.model.Step

class RecipeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val categoryRepository = (application.applicationContext as App).categoryRepository
    private val stepRepository = (application.applicationContext as App).stepRepository

    val data get() = categoryRepository.getAll

    val categories get() = categoryRepository.getCategories()

    fun steps(id: Long) = stepRepository.getAll(id)

    fun insertAll(list: List<Step>, recipeId: Long) {
        val updatedList = list.map {step->
            step.copy(
                id = step.id,
                recipeId = recipeId,
                orderId = list.indexOf(step).toLong() + 1
            )
        }
        stepRepository.deleteAll(recipeId)
        stepRepository.insertAll(updatedList)
    }
}