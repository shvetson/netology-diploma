package ru.shvets.myapplication.db.entity

import androidx.room.*
import org.jetbrains.annotations.NotNull
import ru.shvets.myapplication.model.Step
import ru.shvets.myapplication.utils.Constants

@Entity(
    tableName = "steps",
    indices = [
        Index("recipe_id")
              ],
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipe_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class StepEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,
    @NotNull
    @ColumnInfo(name = "name")
    val name: String,
    @NotNull
    @ColumnInfo(name = "recipe_id")
    val recipeId: Long,
    @NotNull
    @ColumnInfo(name = "order_id")
    val orderId: Long
) {
    fun toStepFromEntity(): Step = Step(
        id = id,
        name = name,
        recipeId = recipeId,
        orderId = orderId
    )

    companion object {
        fun toEntityFromStep(step: Step): StepEntity = StepEntity(
            id = Constants.NEW_STEP_ID,
            name = step.name,
            recipeId = step.recipeId,
            orderId = step.orderId
        )
    }
}
