package ru.shvets.myapplication.ui.recipe_view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.shvets.myapplication.R
import ru.shvets.myapplication.databinding.FragmentTabRecipeBinding
import ru.shvets.myapplication.model.RecipeCategory

private const val ARG_PARAM = "recipe"

class TabRecipeFragment : Fragment(R.layout.fragment_tab_recipe) {
    lateinit var binding: FragmentTabRecipeBinding
    private var recipe: RecipeCategory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipe = arguments?.getParcelable(ARG_PARAM)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTabRecipeBinding.bind(view)

        with(binding) {
            textViewName.setText(recipe?.name)
            textViewAuthor.setText(recipe?.author)
            textViewCategory.setText(recipe?.category)
        }
    }

    companion object {
        fun newInstance(recipe: RecipeCategory) =
            TabRecipeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM, recipe)
                }
            }
    }
}