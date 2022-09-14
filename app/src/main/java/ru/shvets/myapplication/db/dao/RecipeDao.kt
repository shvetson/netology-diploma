package ru.shvets.myapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.shvets.myapplication.db.entity.RecipeEntity
import ru.shvets.myapplication.model.RecipeCategory

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY id ASC")
    fun getAll(): List<RecipeEntity>

//    @Query("SELECT * FROM recipes ORDER BY id ASC")
//    fun getAll(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getById(id: Long): RecipeEntity

    @Query("SELECT a.id as id, a.name as name, a.author as author, a.is_liked as isLiked, b.name as category FROM recipes a INNER JOIN categories b ON a.category_id = b.id AND b.is_checked = 1 ORDER BY a.sort_id ASC")
    fun getRecipes(): LiveData<List<RecipeCategory>>

    @Query("SELECT a.id as id, a.name as name, a.author as author, a.is_liked as isLiked, b.name as category FROM recipes a INNER JOIN categories b ON a.category_id = b.id AND b.is_checked = 1 WHERE a.is_liked = 1 ORDER BY a.sort_id ASC")
    fun getFavorites(): LiveData<List<RecipeCategory>>

    @Query("SELECT a.id as id, a.name as name, a.author as author, a.is_liked as isLiked, b.name as category FROM recipes a INNER JOIN categories b ON a.category_id = b.id AND b.is_checked = 1  WHERE a.name LIKE :searchQuery ORDER BY a.sort_id ASC")
    fun search(searchQuery: String): LiveData<List<RecipeCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: RecipeEntity)

    @Query("DELETE FROM recipes WHERE id = :id")
    fun delete(id: Long)

    @Query(
        """
        UPDATE recipes 
        SET name = :name, author = :author, category_id = :categoryId , sort_id = :sortId
        WHERE id = :id
        """
    )
    fun updateRecipe(id: Long, name: String, author: String, categoryId: Long, sortId: Long)

    @Query(
        """
        UPDATE recipes
        SET is_liked = CASE WHEN is_liked THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    fun updateLiked(id: Long)

    @Query(
        """
            UPDATE recipes
            SET sort_id = :sortId
            WHERE id = :id
        """
    )
    fun updateSortId(sortId: Long, id: Long)

    @Delete
    fun deleteRecipe(recipe: RecipeEntity)
}