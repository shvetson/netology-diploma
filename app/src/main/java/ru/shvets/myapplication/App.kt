package ru.shvets.myapplication

import android.app.Application
import ru.shvets.myapplication.db.AppDatabase
import ru.shvets.myapplication.db.repository.CategoryRepository
import ru.shvets.myapplication.db.repository.RecipeRepository
import ru.shvets.myapplication.db.repository.StepRepository
import ru.shvets.myapplication.db.repository.impl.CategoryRepositoryImpl
import ru.shvets.myapplication.db.repository.impl.RecipeRepositoryImpl
import ru.shvets.myapplication.db.repository.impl.StepRepositoryImpl
import ru.shvets.myapplication.utils.Constants

class App:Application() {
    private lateinit var database: AppDatabase
    lateinit var recipeRepository: RecipeRepository
    lateinit var categoryRepository: CategoryRepository
    lateinit var stepRepository: StepRepository

    override fun onCreate() {
        super.onCreate()

        database = AppDatabase.buildDatabase(applicationContext, Constants.DATABASE_NAME)

        recipeRepository = RecipeRepositoryImpl(database.recipeDao())
        categoryRepository = CategoryRepositoryImpl(database.categoryDao())
        stepRepository = StepRepositoryImpl(database.stepDao())
    }
}