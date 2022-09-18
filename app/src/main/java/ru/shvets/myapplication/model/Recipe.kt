package ru.shvets.myapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.shvets.myapplication.utils.Constants

@Parcelize
data class Recipe(
    val id: Long = Constants.NEW_RECIPE_ID,
    var sortId: Long,
    var name: String,
    val author: String,
    val categoryId: Long,
    val isDefault: Boolean = false,
    var isLiked: Boolean = false,
    val preparation: Int = 0,
    val total: Int = 0,
    val portion: Int = 0,
    val ingredients: String
) : Parcelable
