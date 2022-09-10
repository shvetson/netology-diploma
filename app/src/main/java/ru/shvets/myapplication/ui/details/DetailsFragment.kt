package ru.shvets.myapplication.ui.details

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import ru.shvets.myapplication.R
import ru.shvets.myapplication.adapter.PagerAdapter
import ru.shvets.myapplication.databinding.FragmentDetailsBinding
import ru.shvets.myapplication.model.RecipeCategory
import ru.shvets.myapplication.ui.recipe_view.TabDetailsFragment
import ru.shvets.myapplication.ui.recipe_view.TabRecipeFragment
import ru.shvets.myapplication.utils.Constants

private const val ARG_PARAM = "recipe2"

class DetailsFragment : Fragment(R.layout.fragment_details) {
    lateinit var binding: FragmentDetailsBinding
    private lateinit var adapter: PagerAdapter
    private var currentRecipe: RecipeCategory? = null

    private var fragmentList: ArrayList<Fragment> = arrayListOf()
    private var fragmentTitleList: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentRecipe = arguments?.getParcelable(ARG_PARAM)

        currentRecipe?.let { recipe->
            TabRecipeFragment.newInstance(recipe)
        }?.let { fragment ->
            fragmentList.add(fragment)
        }

        currentRecipe?.let { recipe->
            TabDetailsFragment.newInstance(recipe.id)
        }?.let { fragment ->
            fragmentList.add(fragment)
        }

        fragmentTitleList.add(getString(R.string.title_recipe))
        fragmentTitleList.add(getString(R.string.title_details))

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(Constants.POSITION, binding.tabLayout.selectedTabPosition)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getInt(Constants.POSITION)?.let {
            binding.viewPager2Details.currentItem = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)

        adapter = PagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager2Details.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager2Details) { tab, pos ->
            tab.text = fragmentTitleList[pos]
        }.attach()
    }
}