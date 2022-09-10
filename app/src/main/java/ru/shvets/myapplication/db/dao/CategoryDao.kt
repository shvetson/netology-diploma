package ru.shvets.myapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.shvets.myapplication.db.entity.CategoryEntity
import ru.shvets.myapplication.model.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY id DESC")
    fun getAll(): LiveData<List<CategoryEntity>>

    @Query("SELECT * FROM categories")
    fun getCategories(): List<CategoryEntity>

    @Query("SELECT count(*) FROM categories WHERE is_checked = 0")
    fun count(): Int

    @Query(
        """
        UPDATE categories
        SET is_checked = CASE WHEN is_checked THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    fun updateChecked(id: Long)

    @Query(
        """
        UPDATE categories
        SET is_checked = :flag
        """
    )
    fun updateAllChecked(flag: Int)
}