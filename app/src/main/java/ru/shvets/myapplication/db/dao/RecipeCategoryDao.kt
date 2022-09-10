package ru.shvets.myapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import ru.shvets.myapplication.model.RecipeCategory

@Dao
interface RecipeCategoryDao {
    @Query("SELECT a.id as id, a.name as name, a.author as author, a.is_liked as isLiked, b.name as category FROM recipes a INNER JOIN categories b ON a.category_id = b.id AND b.is_checked = 1")
    fun getAll(): LiveData<List<RecipeCategory>>

    @Query("SELECT a.id as id, a.name as name, a.author as author, a.is_liked as isLiked, b.name as category FROM recipes a INNER JOIN categories b ON a.category_id = b.id AND b.is_checked = 1 WHERE a.is_liked = 1")
    fun getFavorites(): LiveData<List<RecipeCategory>>
}


