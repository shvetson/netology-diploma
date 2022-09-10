package ru.shvets.myapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.shvets.myapplication.db.entity.RecipeEntity

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun getAll(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getById(id: Long): RecipeEntity

    @Query("SELECT COUNT(*) FROM recipes")
    fun getCount(): Int

    @Query("SELECT * FROM recipes WHERE name LIKE :searchQuery ORDER BY id DESC")
    fun search(searchQuery: String): LiveData<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: RecipeEntity)

    @Query("DELETE FROM recipes WHERE id = :id")
    fun delete(id: Long)

    @Query(
        """
        UPDATE recipes 
        SET name = :name, author = :author, category_id = :categoryId 
        WHERE id = :id
        """
    )
    fun updateByID(id: Long, name: String, author: String, categoryId: Long)

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
            SET id = id + 1
            WHERE id >= :id
        """
    )
    fun updateId(id: Long)


//    @Delete
//    fun deleteRecipe(recipe: RecipeEntity)
//
//    @Update
//    fun updateRecipe(recipe: RecipeEntity)
}