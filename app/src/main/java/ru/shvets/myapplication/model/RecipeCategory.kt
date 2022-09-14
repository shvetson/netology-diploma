package ru.shvets.myapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeCategory(
    val id: Long,
    val name: String,
    val author: String,
    val category: String,
    val isLiked: Boolean
): Parcelable