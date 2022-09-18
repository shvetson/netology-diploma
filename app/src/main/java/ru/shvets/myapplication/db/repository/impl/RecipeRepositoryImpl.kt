package ru.shvets.myapplication.db.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.shvets.myapplication.db.dao.RecipeDao
import ru.shvets.myapplication.db.entity.RecipeEntity
import ru.shvets.myapplication.db.repository.RecipeRepository
import ru.shvets.myapplication.model.Recipe
import ru.shvets.myapplication.model.RecipeCategory
import ru.shvets.myapplication.utils.Constants

class RecipeRepositoryImpl(
    private val recipeDao: RecipeDao
) : RecipeRepository {

    override val getAll: List<Recipe> = recipeDao.getAll().map { entity->
        entity.toRecipeFromEntity()
    }

//    override val getAll: LiveData<List<Recipe>> = Transformations.map(recipeDao.getAll()) { entities ->
//        entities.map {
//            it.toRecipeFromEntity()
//        }
//    }

    override fun getAllRecipes(): LiveData<List<RecipeCategory>> =
        Transformations.map(recipeDao.getRecipes()) { entities ->
            entities.map {
                RecipeCategory(
                    id = it.id,
                    name = it.name,
                    author = it.author,
                    isLiked = it.isLiked,
                    category = it.category,
                    preparation = it.preparation,
                    total = it.total,
                    portion = it.portion,
                    ingredients = it.ingredients
                )
            }
        }

    override fun getById(id: Long): Recipe {
        return recipeDao.getById(id).toRecipeFromEntity()
    }

    override fun getFavorites(): LiveData<List<RecipeCategory>> =
        Transformations.map(recipeDao.getFavorites()) { entities ->
            entities.map {
                RecipeCategory(
                    id = it.id,
                    name = it.name,
                    author = it.author,
                    isLiked = it.isLiked,
                    category = it.category,
                    preparation = it.preparation,
                    total = it.total,
                    portion = it.portion,
                    ingredients = it.ingredients
                )
            }
        }

    override fun search(searchQuery: String): LiveData<List<RecipeCategory>> {
        return Transformations.map(recipeDao.search(searchQuery)) { entities ->
            entities.map {
                RecipeCategory(
                    id = it.id,
                    name = it.name,
                    author = it.author,
                    isLiked = it.isLiked,
                    category = it.category,
                    preparation = it.preparation,
                    total = it.total,
                    portion = it.portion,
                    ingredients = it.ingredients
                )
            }
        }
    }

    override fun getMaxSortId(): Int {
        return recipeDao.getMaxSortId()
    }

    override fun delete(id: Long) {
        recipeDao.delete(id)
    }

    override fun insert(recipe: Recipe):Long {
        return recipeDao.insert(RecipeEntity.toEntityFromRecipe(recipe))
    }

    override fun save(recipe: Recipe): Long {
        if (recipe.id == Constants.NEW_RECIPE_ID) {
            return recipeDao.insert(RecipeEntity.toEntityFromRecipe(recipe))
        } else {
            recipeDao.updateRecipe(recipe.id, recipe.name, recipe.author, recipe.categoryId, recipe.id, recipe.preparation, recipe.total, recipe.portion, recipe.ingredients)
            return recipe.id
        }
    }

    override fun updateLiked(recipe: RecipeCategory) {
        recipeDao.updateLiked(recipe.id)
    }

    override fun updateSortId(sortId: Long, id: Long) {
        recipeDao.updateSortId(sortId, id)
    }

    override fun remove(recipe: Recipe, onSuccess: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun updateRecipe(id: Long, name: String, author: String, categoryId: Long, sortId: Long, preparation: Int, total: Int, portion: Int, ingredients: String) {
        recipeDao.updateRecipe(id, name, author, categoryId, sortId, preparation, total, portion, ingredients)
    }
}