package ru.shvets.myapplication.ui.recipe

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.CustomPopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.shvets.myapplication.R
import ru.shvets.myapplication.adapter.StepAdapter
import ru.shvets.myapplication.databinding.DialogStepBinding
import ru.shvets.myapplication.databinding.FragmentRecipeBinding
import ru.shvets.myapplication.model.Category
import ru.shvets.myapplication.model.Recipe
import ru.shvets.myapplication.model.RecipeCategory
import ru.shvets.myapplication.model.Step
import ru.shvets.myapplication.utils.Constants
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM = "recipe"

class RecipeFragment : Fragment(R.layout.fragment_recipe) {
    private lateinit var binding: FragmentRecipeBinding
    private val recipeViewModel: RecipeViewModel by viewModels()
    private lateinit var actionBar: ActionBar
    private lateinit var stepAdapter: StepAdapter
    private lateinit var currentCategory: Category
    private var currentRecipe: RecipeCategory? = null
    private val stepList: ArrayList<Step> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar = (activity as AppCompatActivity).supportActionBar!!
        currentRecipe = arguments?.getParcelable(ARG_PARAM)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipeBinding.bind(view)
        actionBar.title =
            if (currentRecipe == null) getString(R.string.title_add) else getString(R.string.title_edit_recipe)

        binding.textEditName.requestFocus()

        val spinnerCategoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            android.R.id.text1,
            recipeViewModel.categories
        )

        binding.spinnerCategory.setAdapter(spinnerCategoryAdapter)

        binding.spinnerCategory.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                spinnerCategoryAdapter.getItem(position)?.let {
                    currentCategory = it
                }
            }

        stepAdapter = StepAdapter(stepList)
        binding.viewListSteps.adapter = stepAdapter

        if (currentRecipe == null) {
            changeVisibilityImage()
        }

        currentRecipe?.let {
            recipeViewModel.steps(it.id).observe(viewLifecycleOwner) { list ->
                stepList.clear()
                stepList.addAll(list)
                stepAdapter.notifyDataSetChanged()
                changeVisibilityImage()
            }
        }

        binding.viewListSteps.setOnItemClickListener { parent, view, position, _ ->
            val step = parent.getItemAtPosition(position) as Step
            showPopupMenu(view, step, position)
        }

        binding.buttonAdd.setOnClickListener {
//            requestFocusAndShowSoftInput(binding.viewListSteps)
            binding.textEditIngredients.clearFocus()
            addStep()
        }

        if (currentRecipe != null) {
            with(binding) {
                textEditName.setText(currentRecipe!!.name)
                textEditAuthor.setText(currentRecipe!!.author)

                for (i in 0 until spinnerCategory.adapter.count) {
                    val category = spinnerCategory.adapter.getItem(i) as Category
                    if (category.name == currentRecipe!!.category) {
                        binding.spinnerCategory.setText(category.name, false)
                        binding.spinnerCategory.setSelection(category.name.length)
                        currentCategory = category
                        break
                    }
                }
                imageViewLiked.setChecked(currentRecipe!!.isLiked)
                textEditPreparation.setText(currentRecipe!!.preparation.toString())
                textEditTotal.setText(currentRecipe!!.total.toString())
                textEditPortion.setText(currentRecipe!!.portion.toString())
                textEditIngredients.setText(currentRecipe!!.ingredients)
            }
        }

        with(binding) {
            buttonSave.setOnClickListener {
                if (textEditName.text?.isBlank() == true) {
                    textEditName.error = getString(R.string.error_empty)
                } else {

                    if (spinnerCategory.text.isBlank()) {
                        spinnerCategory.error = getString(R.string.error_empty)
                    } else {
                        val id = currentRecipe?.id ?: Constants.NEW_RECIPE_ID

                        val newRecipe = Recipe(
                            id = id,
                            sortId = (recipeViewModel.getMaxSortId() + 1).toLong(),
                            name = textEditName.text.toString(),
                            author = textEditAuthor.text.toString(),
                            categoryId = currentCategory.id,
                            preparation = if (textEditPreparation.text.toString().isNotBlank()) textEditPreparation.text.toString().toInt() else 0,
                            total = if (textEditTotal.text.toString().isNotBlank()) textEditTotal.text.toString().toInt() else 0,
                            portion = if (textEditPortion.text.toString().isNotBlank()) textEditPortion.text.toString().toInt() else 0,
                            ingredients = textEditIngredients.text.toString()
                        )

                        if (stepList.size == 0) {
                            stepList.add(
                                Step(
                                    id = Constants.NEW_STEP_ID,
                                    name = getString(R.string.default_row_empty_list),
                                    recipeId = 0,
                                    orderId = 0
                                )
                            )
                        }
                        setFragmentResult(
                            requestKey = REQUEST_KEY,
                            bundleOf(
                                RESULT_KEY1 to newRecipe,
                                RESULT_KEY2 to stepList
                            )
                        )
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun showPopupMenu(view: View, step: Step, position: Int) {
        val context: Context = view.context

        val popupMenu by lazy {
            CustomPopupMenu(context, view).apply {
                inflate(R.menu.menu_step_options)
                this.menu.findItem(R.id.menu_move_down).isEnabled = position < stepList.size - 1
                this.menu.findItem(R.id.menu_move_up).isEnabled = position > 0
            }
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_edit -> {
                    editStep(step)
                    true
                }
                R.id.menu_delete -> {
                    deleteStep(stepList, step)
                    true
                }
                R.id.menu_move_up -> {
                    moveStep(stepList, step, -1)
                    true
                }
                R.id.menu_move_down -> {
                    moveStep(stepList, step, 1)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun addStep() {
        val dialogBinding = DialogStepBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.title_message_enter_step))
            .setView(dialogBinding.root)
            .setPositiveButton(getString(R.string.btn_label_add)) { dialog, which ->
                val name = dialogBinding.editTextAddStepProcess.text.toString()
                if (name.isNotBlank()) {
                    recipeViewModel.add(stepList, name)
                    stepAdapter.notifyDataSetChanged()
                    changeVisibilityImage()
                }
            }
            .create()
        dialog.show()
    }

    private fun editStep(step: Step) {
        val dialogBinding = DialogStepBinding.inflate(layoutInflater)
        dialogBinding.editTextAddStepProcess.setText(step.name)
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.title_edit_step))
            .setView(dialogBinding.root)
            .setPositiveButton(getString(R.string.btn_label_confirm), null)
            .create()
        dialog.setOnShowListener {
            dialogBinding.editTextAddStepProcess.requestFocus()

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val enterText = dialogBinding.editTextAddStepProcess.text.toString()
                if (enterText.isBlank()) {
                    dialogBinding.editTextAddStepProcess.error = getString(R.string.error_empty)
                    return@setOnClickListener
                }
                updateStep(stepList, step, enterText)
                stepAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun deleteStep(list: ArrayList<Step>, step: Step) {
        val listener = DialogInterface.OnClickListener { dialog, which ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                recipeViewModel.delete(list, step)
                stepAdapter.notifyDataSetChanged()
                changeVisibilityImage()
            }
        }

        val dialog = AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.title_message_question))
            .setMessage(getString(R.string.question_delete))
            .setPositiveButton(getString(R.string.btn_label_delete), listener)
            .setNegativeButton(R.string.btn_label_cancel, listener)
            .create()
        dialog.show()
    }

    private fun moveStep(list: ArrayList<Step>, step: Step, moveBy: Int) {
        recipeViewModel.move(list, step, moveBy)
        stepAdapter.notifyDataSetChanged()
    }

    private fun updateStep(list: ArrayList<Step>, step: Step, newName: String) {
        recipeViewModel.update(list, step, newName)
        stepAdapter.notifyDataSetChanged()
    }

    private fun ImageView.setChecked(isLiked: Boolean) {
        val icon = when (isLiked) {
            true -> R.drawable.ic_favorite_fill
            false -> R.drawable.ic_favorite_border
        }
        setImageResource(icon)
    }

    private fun changeVisibilityImage() {
        if (stepList.size == 0) {
            binding.imageViewEmpty.visibility = View.VISIBLE
        } else {
            binding.imageViewEmpty.visibility = View.INVISIBLE
        }
    }

    companion object {
        const val REQUEST_KEY = "NEW_RECIPE"
        const val RESULT_KEY1 = "NEW_RECIPE"
        const val RESULT_KEY2 = "NEW_STEPS"
    }
}