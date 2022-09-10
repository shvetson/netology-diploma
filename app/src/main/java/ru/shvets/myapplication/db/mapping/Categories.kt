package ru.shvets.myapplication.db.mapping

import ru.shvets.myapplication.db.entity.CategoryEntity
import ru.shvets.myapplication.model.Category

// не используется, mapping прописаны с сущностях

fun CategoryEntity.toModel() = Category(
    id = id,
    name = name,
    isChecked = isChecked,
    isDefault = isDefault
)

fun Category.toEntity() = CategoryEntity(
    id = id,
    name = name,
    isChecked = isChecked,
    isDefault = isDefault
)