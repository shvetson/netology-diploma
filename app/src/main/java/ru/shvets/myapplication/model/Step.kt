package ru.shvets.myapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Step(
    val id: Long,
    val name: String,
    val recipeId: Long,
    val orderId: Long
) : Parcelable