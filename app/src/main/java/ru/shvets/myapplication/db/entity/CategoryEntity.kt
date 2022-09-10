package ru.shvets.myapplication.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.shvets.myapplication.model.Category
import ru.shvets.myapplication.utils.Constants

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "is_checked")
    val isChecked: Boolean,
    @ColumnInfo(name = "is_default")
    val isDefault: Boolean
) {
    fun toCategoryFromEntity(): Category = Category(
        id = id,
        name = name,
        isDefault = isDefault,
        isChecked = isChecked
    )

    companion object {
        fun toEntityFromCategory(category: Category): CategoryEntity = CategoryEntity(
            id = Constants.NEW_CATEGORY_ID,
            name = category.name,
            isDefault = category.isDefault,
            isChecked = category.isChecked
        )
    }
}