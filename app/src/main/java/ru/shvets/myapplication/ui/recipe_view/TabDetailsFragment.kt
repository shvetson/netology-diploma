package ru.shvets.myapplication.ui.recipe_view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ru.shvets.myapplication.R
import ru.shvets.myapplication.adapter.StepAdapter
import ru.shvets.myapplication.databinding.FragmentTabDetailsBinding
import ru.shvets.myapplication.model.RecipeCategory
import ru.shvets.myapplication.model.Step
import ru.shvets.myapplication.ui.recipe.RecipeViewModel

private const val ARG_PARAM = "recipeId"

class TabDetailsFragment : Fragment(R.layout.fragment_tab_details) {
    lateinit var binding: FragmentTabDetailsBinding
    private val recipeViewModel: RecipeViewModel by viewModels()
    private var recipeId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeId = arguments?.getLong(ARG_PARAM) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTabDetailsBinding.bind(view)

        val list = arrayListOf<Step>()
        val adapter = StepAdapter(list)
        binding.listViewDetails.adapter = adapter

        recipeViewModel.steps(recipeId).observe(viewLifecycleOwner) { it ->
            list.addAll(it)
            adapter.notifyDataSetChanged()

            if (it.isEmpty()) {
                binding.imageViewIcon.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        fun newInstance(recipeId: Long) =
            TabDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM, recipeId)
                }
            }
    }
}