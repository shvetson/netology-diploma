package ru.shvets.myapplication.db.mapping

import ru.shvets.myapplication.db.entity.RecipeEntity
import ru.shvets.myapplication.model.Recipe

// не используется, mapping прописаны с сущностях

fun RecipeEntity.toModel() = Recipe(
    id = id,
    name = name,
    author = author,
    categoryId = categoryId,
    isDefault = isDefault,
    isLiked = isLiked)

fun Recipe.toEntity() = RecipeEntity(
    id = id,
    name = name,
    author = author,
    categoryId = categoryId,
    isDefault = isDefault,
    isLiked = isLiked)