package ru.shvets.myapplication.db.entity

import androidx.annotation.NonNull
import androidx.room.*
import org.jetbrains.annotations.NotNull
import ru.shvets.myapplication.model.Recipe
import ru.shvets.myapplication.utils.Constants

@Entity(
    tableName = "recipes",
    indices = [
        Index("name")
    ],
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,
    @NotNull
    @ColumnInfo(name = "name")
    val name: String,
    @NotNull
    @ColumnInfo(name = "author")
    val author: String,
    @NotNull
    @ColumnInfo(name = "category_id")
    val categoryId: Long,
    @NotNull
    @ColumnInfo(name = "is_default")
    val isDefault: Boolean = false,
    @NotNull
    @ColumnInfo(name = "is_liked")
    val isLiked: Boolean = false
) {
    fun toRecipeFromEntity(): Recipe = Recipe(
        id = id,
        name = name,
        author = author,
        categoryId = categoryId,
        isDefault = isDefault,
        isLiked = isLiked)

    companion object{
        fun toEntityFromRecipe(recipe: Recipe): RecipeEntity = RecipeEntity(
            id = Constants.NEW_RECIPE_ID,
            name = recipe.name,
            author = recipe.author,
            categoryId = recipe.categoryId,
            isDefault = recipe.isDefault,
            isLiked = recipe.isLiked)
    }
}