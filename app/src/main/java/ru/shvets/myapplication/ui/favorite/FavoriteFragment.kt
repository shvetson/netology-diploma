package ru.shvets.myapplication.ui.favorite

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ru.shvets.myapplication.R
import ru.shvets.myapplication.adapter.FavoriteAdapter
import ru.shvets.myapplication.adapter.RecipeActionListener
import ru.shvets.myapplication.databinding.FragmentFavoriteBinding
import ru.shvets.myapplication.model.RecipeCategory
import ru.shvets.myapplication.ui.MainViewModel

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var actionBar: ActionBar

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar = (activity as AppCompatActivity).supportActionBar!!
        actionBar.setTitle(R.string.title_favorite)

        setupMenu()

        favoriteAdapter = FavoriteAdapter(object : RecipeActionListener {
            override fun onLikeClicked(recipe: RecipeCategory) {
            }

            override fun onItemClicked(recipe: RecipeCategory) {
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())

            val divider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            addItemDecoration(divider)

            adapter = favoriteAdapter
        }

        mainViewModel.getFavorites().observe(viewLifecycleOwner) { list ->
            favoriteAdapter.differ.submitList(list.toMutableList())

            if (list.isEmpty()) {
                binding.imageViewEmpty.visibility = View.VISIBLE
            }
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                val item: MenuItem = menu.findItem(R.id.action_search)
                item.isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        })
    }
}