package ru.shvets.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.shvets.myapplication.databinding.ItemRecipeBinding
import ru.shvets.myapplication.model.RecipeCategory

class RecipeAdapter(
    private val recipeActionListener: RecipeActionListener
    ): ListAdapter<RecipeCategory, RecipeAdapter.RecipeViewHolder>(RecipeDiffCallback()) {

    inner class RecipeViewHolder(
        private val binding: ItemRecipeBinding,
        private val recipeActionListener: RecipeActionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: RecipeCategory) {
            binding.apply {
                textViewName.text = recipe.name
                textViewAuthor.text = recipe.author
                textViewCategory.text = recipe.category
                buttonLiked.isChecked = recipe.isLiked
            }

            binding.root.setOnClickListener{
                recipeActionListener.onItemClicked(recipe)
                return@setOnClickListener
            }

            binding.buttonLiked.setOnClickListener {
                recipeActionListener.onLikeClicked(recipe)
                return@setOnClickListener
            }
        }
    }

    class RecipeDiffCallback : DiffUtil.ItemCallback<RecipeCategory>() {
        override fun areItemsTheSame(oldItem: RecipeCategory, newItem: RecipeCategory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecipeCategory, newItem: RecipeCategory): Boolean {
            return oldItem == newItem
        }

        // Для исключения блика элемента при нажатии на checkButton liked (в частности)
        override fun getChangePayload(oldItem: RecipeCategory, newItem: RecipeCategory): Any? {
            if (oldItem.isLiked != newItem.isLiked) return false
            return super.getChangePayload(oldItem, newItem)
        }
    }

    val differ = AsyncListDiffer(this, RecipeDiffCallback())

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding, recipeActionListener)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
//        holder.bind(getItem(position))
    }
}