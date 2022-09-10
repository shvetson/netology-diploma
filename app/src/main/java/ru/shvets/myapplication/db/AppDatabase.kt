package ru.shvets.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.shvets.myapplication.db.dao.CategoryDao
import ru.shvets.myapplication.db.dao.RecipeCategoryDao
import ru.shvets.myapplication.db.dao.RecipeDao
import ru.shvets.myapplication.db.dao.StepDao
import ru.shvets.myapplication.db.entity.CategoryEntity
import ru.shvets.myapplication.db.entity.RecipeEntity
import ru.shvets.myapplication.db.entity.StepEntity

@Database(
    entities = [
        CategoryEntity::class,
        RecipeEntity::class,
        StepEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun categoryDao(): CategoryDao
    abstract fun recipeCategoryDao(): RecipeCategoryDao
    abstract fun stepDao(): StepDao

    companion object {
        fun buildDatabase(context: Context, dbName: String): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, dbName)
                .allowMainThreadQueries()
                .createFromAsset("database/recipes.db")
                .build()
        }
    }
}