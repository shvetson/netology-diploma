package ru.shvets.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.shvets.myapplication.databinding.ItemCategoryBinding
import ru.shvets.myapplication.model.Category

class CategoryAdapter(
    private val categoryActionListener: CategoryActionListener
    ): ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding,
        private val categoryActionListener: CategoryActionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.apply {
                textViewName.text = category.name
                checkBox.isChecked = category.isChecked
            }

            binding.root.setOnClickListener{
                categoryActionListener.onItemClicked(category)
                return@setOnClickListener
            }

            binding.checkBox.setOnClickListener {
                categoryActionListener.onCheckedClicked(category)
                return@setOnClickListener
            }
        }
    }

    class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Category, newItem: Category): Any? {
            if (oldItem.isChecked != newItem.isChecked) return false
            return super.getChangePayload(oldItem, newItem)
        }
    }

    val differ = AsyncListDiffer(this, CategoryDiffCallback())

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding, categoryActionListener)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }
}