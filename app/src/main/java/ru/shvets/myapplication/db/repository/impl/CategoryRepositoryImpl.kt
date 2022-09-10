package ru.shvets.myapplication.db.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.shvets.myapplication.model.Category
import ru.shvets.myapplication.db.dao.CategoryDao
import ru.shvets.myapplication.db.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val dao: CategoryDao
) : CategoryRepository {

    override val getAll: LiveData<List<Category>> = Transformations.map(dao.getAll()) { entities ->
        entities.map {
            it.toCategoryFromEntity()
        }
    }

    override fun getCategories(): List<Category> {
        return dao.getCategories().map {
            it.toCategoryFromEntity()
        }
    }

    override fun count(): Int {
        return dao.count()
    }

    override suspend fun updateChecked(category: Category) {
        dao.updateChecked(category.id)
    }

    override suspend fun updateAllChecked(flag: Boolean) {
        dao.updateAllChecked(if (flag) 1 else 0)
    }
}