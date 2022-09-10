package ru.shvets.myapplication.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.shvets.myapplication.utils.Constants

@Parcelize
data class Category(
    val id: Long = Constants.NEW_CATEGORY_ID,
    val name: String,
    var isChecked: Boolean = false,
    val isDefault: Boolean = true
): Parcelable {
    override fun toString(): String {
        return name
    }
}