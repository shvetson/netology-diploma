package ru.shvets.myapplication.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import ru.shvets.myapplication.R
import ru.shvets.myapplication.adapter.CategoryActionListener
import ru.shvets.myapplication.adapter.CategoryAdapter
import ru.shvets.myapplication.databinding.FragmentFeedBinding
import ru.shvets.myapplication.databinding.FragmentFilterBinding
import ru.shvets.myapplication.model.Category
import kotlin.properties.Delegates

class FilterFragment : Fragment(R.layout.fragment_filter) {
    private val filterViewModel: FilterViewModel by viewModels()

    private lateinit var binding: FragmentFilterBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var actionBar: ActionBar
    private var count by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoryAdapter = CategoryAdapter(object : CategoryActionListener {
            override fun onCheckedClicked(category: Category) {
                filterViewModel.updateChecked(category)

                if (category.isChecked && binding.checkBoxAll.isChecked)
                binding.checkBoxAll.isChecked = false
            }

            override fun onItemClicked(category: Category) {
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFilterBinding.bind(view)

        count = filterViewModel.count()
        binding.checkBoxAll.isChecked = count == 0

        actionBar = (activity as AppCompatActivity).supportActionBar!!
        actionBar.setTitle(R.string.title_filter)

        binding.recyclerViewCategory.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            val divider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            addItemDecoration(divider)
            adapter = categoryAdapter
        }

        filterViewModel.data.observe(viewLifecycleOwner, Observer { list ->
            categoryAdapter.differ.submitList(list)
        })

        binding.checkBoxAll.setOnCheckedChangeListener { button, isChecked ->
            button.setOnClickListener {
                filterViewModel.updateAll(isChecked)
            }
        }
    }
}