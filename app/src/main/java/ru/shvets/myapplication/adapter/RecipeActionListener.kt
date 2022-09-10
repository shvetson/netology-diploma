package ru.shvets.myapplication.adapter

import ru.shvets.myapplication.model.RecipeCategory

interface RecipeActionListener {
    //Обработка кликов на элементе списка
    fun onLikeClicked(recipe: RecipeCategory)
    fun onItemClicked(recipe: RecipeCategory)
}