package ru.shvets.myapplication.ui.filter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.shvets.myapplication.App
import ru.shvets.myapplication.model.Category

class FilterViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val categoryRepository = (application.applicationContext as App).categoryRepository

    val data get() = categoryRepository.getAll

    val categories get() = categoryRepository.getCategories()

    fun updateAll(flag: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.updateAllChecked(flag)
        }
    }

    fun updateChecked(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.updateChecked(category)
        }
    }

    fun count(): Int {
        return categoryRepository.count()
    }
}