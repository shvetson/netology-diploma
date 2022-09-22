package ru.shvets.myapplication.ui.feed

import android.graphics.Canvas
import android.os.Bundle
import android.text.InputType
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import ru.shvets.myapplication.R
import ru.shvets.myapplication.adapter.RecipeActionListener
import ru.shvets.myapplication.adapter.RecipeAdapter
import ru.shvets.myapplication.databinding.FragmentFeedBinding
import ru.shvets.myapplication.model.Recipe
import ru.shvets.myapplication.model.RecipeCategory
import ru.shvets.myapplication.model.Step
import ru.shvets.myapplication.ui.MainViewModel
import ru.shvets.myapplication.ui.recipe.RecipeFragment

class FeedFragment : Fragment(R.layout.fragment_feed) {
    private lateinit var binding: FragmentFeedBinding
    private lateinit var recipeAdapter: RecipeAdapter

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recipeAdapter = RecipeAdapter(object : RecipeActionListener {
            override fun onLikeClicked(recipe: RecipeCategory) {
                mainViewModel.updateLiked(recipe)
            }

            override fun onItemClicked(recipe: RecipeCategory) {
                val direction = FeedFragmentDirections.actionFeedFragmentToDetailsFragment(recipe)
                findNavController().navigate(direction)
            }
        })

        setFragmentResultListener(requestKey = RecipeFragment.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey != RecipeFragment.REQUEST_KEY) return@setFragmentResultListener
            val newRecipe = bundle.getParcelable<Recipe>(RecipeFragment.RESULT_KEY1)
                ?: return@setFragmentResultListener
            val newSteps = bundle.getParcelableArrayList<Step>(RecipeFragment.RESULT_KEY2)
                ?: return@setFragmentResultListener

            val recipeId: Long = mainViewModel.save(newRecipe)
            mainViewModel.insertAll(list = newSteps, recipeId = recipeId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFeedBinding.bind(view)

        binding.recyclerView.apply {
            (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
            layoutManager = LinearLayoutManager(requireActivity())
            adapter?.setHasStableIds(true)
            adapter = recipeAdapter

            val divider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            addItemDecoration(divider)
        }

        mainViewModel.getAllRecipes().observe(viewLifecycleOwner) { list ->
            recipeAdapter.differ.submitList(list)

            if (list.isEmpty()) {
                binding.imageViewEmpty.visibility = View.VISIBLE
            }
            val title =
                getString(R.string.app_name) + ", " + if (list.isNotEmpty()) list.size.toString() else ""
            (activity as AppCompatActivity).supportActionBar?.title = title
        }

        binding.fabAddUser.setOnClickListener {
            val direction = FeedFragmentDirections.actionFeedFragmentToRecipeFragment(null)
            findNavController().navigate(direction)

//            findNavController().navigate(R.id.action_feedFragment_to_userFragment)
        }

        val itemMoveCallback = ItemMoveCallback()
        val itemTouchHelper = ItemTouchHelper(itemMoveCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        setupMenu()
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                val item: MenuItem = menu.findItem(R.id.action_search)
                item.isEnabled = true
                item.isVisible = true
                val searchView = item.actionView as SearchView
                searchView.inputType = InputType.TYPE_CLASS_TEXT

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query != null) {
                            searchDatabase(query)
                        }
                        return false
                    }

                    override fun onQueryTextChange(query: String?): Boolean {
                        if (query != null) {
                            searchDatabase(query)
                        }
                        return true
                    }

                    private fun searchDatabase(query: String) {
                        val searchQuery = "%$query%"
                        val list = mainViewModel.search(searchQuery)
                        recipeAdapter.differ.submitList(list)
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    inner class ItemMoveCallback : ItemTouchHelper.Callback() {

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        override fun isItemViewSwipeEnabled(): Boolean {
            return true
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val startPosition = viewHolder.absoluteAdapterPosition
            val endPosition = target.absoluteAdapterPosition

            if (startPosition != RecyclerView.NO_POSITION ||
                endPosition != RecyclerView.NO_POSITION
            ) {
                if (startPosition != endPosition) {
                    val recipeStart =
                        mainViewModel.getRecipe(recipeAdapter.differ.currentList[startPosition].id)
                    val recipeEnd =
                        mainViewModel.getRecipe(recipeAdapter.differ.currentList[endPosition].id)
                    mainViewModel.updateDragDrop(recipeStart, recipeEnd)
                }
            }
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.absoluteAdapterPosition
            val currentRecipe = recipeAdapter.differ.currentList[position]

            when (direction) {
                ItemTouchHelper.LEFT -> {
                    mainViewModel.delete(currentRecipe)
//                    binding.recyclerView.adapter?.notifyItemRemoved(position)
                    Toast.makeText(
                        requireActivity(),
                        "${currentRecipe.name} ${getString(R.string.recipe_is_deleted)}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                ItemTouchHelper.RIGHT -> {
                    val direction =
                        FeedFragmentDirections.actionFeedFragmentToRecipeFragment(currentRecipe)
                    findNavController().navigate(direction)
                    binding.recyclerView.adapter!!.notifyItemChanged(position)
                }
            }
        }

        override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder) = 0.3F

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)

            if (actionState == ACTION_STATE_DRAG) {
                viewHolder?.itemView?.alpha = 0.5f
            }
        }

        override fun clearView(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ) {
            super.clearView(recyclerView, viewHolder)
            viewHolder.itemView.alpha = 1.0f
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val context = viewHolder.itemView.context
            RecyclerViewSwipeDecorator.Builder(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
                .addSwipeLeftActionIcon(R.drawable.ic_delete)
                .addSwipeLeftBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.swipe_delete
                    )
                )
                .addSwipeLeftLabel(getString(R.string.swipe_delete))
                .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                .setSwipeLeftLabelColor(R.color.primaryTextColor)
                .addSwipeLeftCornerRadius(TypedValue.COMPLEX_UNIT_SP, 8F)
                .addSwipeRightActionIcon(R.drawable.ic_edit)
                .addSwipeRightBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.swipe_edit
                    )
                )
                .addSwipeRightLabel(getString(R.string.swipe_edit))
                .setSwipeRightLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                .setSwipeRightLabelColor(R.color.primaryTextColor)
                .addSwipeRightCornerRadius(TypedValue.COMPLEX_UNIT_SP, 8F)
                .create()
                .decorate()

            super.onChildDraw(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }
}