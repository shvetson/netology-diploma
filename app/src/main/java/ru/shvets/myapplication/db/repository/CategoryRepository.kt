package ru.shvets.myapplication.db.repository

import androidx.lifecycle.LiveData
import ru.shvets.myapplication.model.Category

interface CategoryRepository {
    val getAll: LiveData<List<Category>>
    fun getCategories(): List<Category>
    fun count(): Int
    suspend fun updateChecked(category: Category)
    suspend fun updateAllChecked(flag: Boolean)
}