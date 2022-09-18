package ru.shvets.myapplication.db.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.shvets.myapplication.db.dao.StepDao
import ru.shvets.myapplication.db.entity.StepEntity
import ru.shvets.myapplication.db.repository.StepRepository
import ru.shvets.myapplication.model.Step

class StepRepositoryImpl (
    private val dao: StepDao,
) : StepRepository {

    override fun getAll(id: Long): LiveData<List<Step>> =
        Transformations.map(dao.getAll(recipeId = id)) { entities ->
            entities.map {entity->
                entity.toStepFromEntity()
            }
        }

    override fun getAllSteps(id: Long): List<Step> =
        dao.getAllSteps(recipeId = id).map { entity ->
            entity.toStepFromEntity()
        }


    override fun insertAll(list: List<Step>) {
        list.map{step->
            dao.insert(StepEntity.toEntityFromStep(step))
        }
    }

    override fun deleteAll(recipeId: Long) {
        dao.deleteAll(recipeId)
    }
}