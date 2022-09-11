package ru.shvets.myapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import ru.shvets.myapplication.model.RecipeCategory

@Dao
interface RecipeCategoryDao {
    @Query("SELECT a.id as id, a.name as name, a.author as author, a.is_liked as isLiked, b.name as category FROM recipes a INNER JOIN categories b ON a.category_id = b.id AND b.is_checked = 1 ORDER BY a.id ASC")
    fun getAll(): LiveData<List<RecipeCategory>>

    @Query("SELECT a.id as id, a.name as name, a.author as author, a.is_liked as isLiked, b.name as category FROM recipes a INNER JOIN categories b ON a.category_id = b.id AND b.is_checked = 1 WHERE a.is_liked = 1 ORDER BY a.id ASC")
    fun getFavorites(): LiveData<List<RecipeCategory>>

    @Query("SELECT a.id as id, a.name as name, a.author as author, a.is_liked as isLiked, b.name as category FROM recipes a INNER JOIN categories b ON a.category_id = b.id AND b.is_checked = 1  WHERE a.name LIKE :searchQuery ORDER BY a.id ASC")
    fun search(searchQuery: String): LiveData<List<RecipeCategory>>
}


