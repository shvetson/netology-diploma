package ru.shvets.myapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.shvets.myapplication.db.entity.StepEntity
import ru.shvets.myapplication.model.Step

@Dao
interface StepDao  {
    @Query("SELECT * FROM steps WHERE recipe_id = :recipeId ORDER BY order_id ASC")
    fun getAll(recipeId: Long): LiveData<List<StepEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(step: StepEntity)

    @Query("DELETE FROM steps WHERE recipe_id = :recipeId")
    fun deleteAll(recipeId: Long)
}