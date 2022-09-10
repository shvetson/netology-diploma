package ru.shvets.myapplication.db.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.shvets.myapplication.db.dao.RecipeCategoryDao
import ru.shvets.myapplication.model.Recipe
import ru.shvets.myapplication.db.dao.RecipeDao
import ru.shvets.myapplication.db.entity.RecipeEntity
import ru.shvets.myapplication.db.repository.RecipeRepository
import ru.shvets.myapplication.model.RecipeCategory
import ru.shvets.myapplication.utils.Constants

class RecipeRepositoryImpl(
    private val recipeDao: RecipeDao,
    private val recipeCategoryDao: RecipeCategoryDao
) : RecipeRepository {

    override val getAll: LiveData<List<Recipe>> = Transformations.map(recipeDao.getAll()) { entities ->
        entities.map {
            it.toRecipeFromEntity()
        }
    }

    override fun getAllRecipes(): LiveData<List<RecipeCategory>> =
        Transformations.map(recipeCategoryDao.getAll()) { entities ->
            entities.map {
                RecipeCategory(
                    id = it.id,
                    name = it.name,
                    author = it.author,
                    isLiked = it.isLiked,
                    category = it.category,
                )
            }
        }

    override fun getById(id: Long): Recipe {
        return recipeDao.getById(id).toRecipeFromEntity()
    }

    override suspend fun getCount(): Int {
        return recipeDao.getCount()
    }

    override fun getFavorites(): LiveData<List<RecipeCategory>> =
        Transformations.map(recipeCategoryDao.getFavorites()) { entities ->
            entities.map {
                RecipeCategory(
                    id = it.id,
                    name = it.name,
                    author = it.author,
                    isLiked = it.isLiked,
                    category = it.category,
                )
            }
        }

    override fun search(searchQuery: String): LiveData<List<Recipe>> {
        return Transformations.map(recipeDao.search(searchQuery)) { entities ->
            entities.map {
                it.toRecipeFromEntity()
            }
        }
    }

    override suspend fun delete(recipe: RecipeCategory) {
        recipeDao.delete(recipe.id)
    }

    override suspend fun insert(recipe: Recipe) {
        recipeDao.insert(RecipeEntity.toEntityFromRecipe(recipe))
    }

    override suspend fun save(recipe: Recipe) {
        if (recipe.id == Constants.NEW_RECIPE_ID) {
            recipeDao.insert(RecipeEntity.toEntityFromRecipe(recipe))
        } else {
            recipeDao.updateByID(recipe.id, recipe.name, recipe.author, recipe.categoryId)
        }
    }

    override suspend fun updateLiked(recipe: RecipeCategory) {
        recipeDao.updateLiked(recipe.id)
    }

    override suspend fun updateId(recipe: Recipe) {
        recipeDao.updateId(recipe.id)
    }

    override suspend fun remove(recipe: Recipe, onSuccess: () -> Unit) {
        TODO("Not yet implemented")
    }
}