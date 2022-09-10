package ru.shvets.myapplication.adapter

import ru.shvets.myapplication.model.Category

interface CategoryActionListener {
    //Обработка кликов на элементе списка
    fun onCheckedClicked(category: Category)
    fun onItemClicked(category: Category)
}