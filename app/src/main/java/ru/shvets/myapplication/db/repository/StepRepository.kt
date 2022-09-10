package ru.shvets.myapplication.db.repository

import androidx.lifecycle.LiveData
import ru.shvets.myapplication.model.Step

interface StepRepository {
        fun getAll(id: Long): LiveData<List<Step>>
        fun insertAll(list: List<Step>)
        fun deleteAll(recipeId: Long)
}