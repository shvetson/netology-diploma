package ru.shvets.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.shvets.myapplication.R
import ru.shvets.myapplication.databinding.ItemRecipeBinding
import ru.shvets.myapplication.model.RecipeCategory

class RecipeAdapter(
    private val listener: RecipeActionListener
) : ListAdapter<RecipeCategory, RecipeAdapter.RecipeViewHolder>(RecipeDiffCallback()),
    View.OnClickListener {

    class RecipeViewHolder(
        val binding: ItemRecipeBinding
    ) : RecyclerView.ViewHolder(binding.root)

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

        binding.buttonLiked.setOnClickListener(this)
        binding.root.setOnClickListener(this)

        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = differ.currentList[position]

        with(holder.binding) {
            buttonLiked.tag = recipe
            root.tag = recipe

            textViewName.text = recipe.name
            textViewAuthor.text = recipe.author
            textViewCategory.text = recipe.category
            buttonLiked.isChecked = recipe.isLiked
        }
    }

    override fun onClick(view: View) {
        val recipe = view.tag as RecipeCategory

        when (view.id) {
            R.id.button_liked -> listener.onLikeClicked(recipe)
            else -> listener.onItemClicked(recipe)
        }
    }
}