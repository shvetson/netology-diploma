package ru.shvets.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import ru.shvets.myapplication.databinding.ItemStepBinding
import ru.shvets.myapplication.model.Step

class StepAdapter(
    private val steps: ArrayList<Step>
): BaseAdapter(){

    override fun getCount(): Int {
        return steps.size
    }

    override fun getItem(position: Int): Step {
        return steps[position]
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemStepBinding =
            convertView?.tag as ItemStepBinding? ?:
            createBinding(parent.context)

        val step: Step = getItem(position)
        binding.itemSpinner.text = "${position+1} ${step.name}"
        return binding.root
    }

    private fun createBinding(context: Context): ItemStepBinding {
        val binding : ItemStepBinding = ItemStepBinding.inflate(LayoutInflater.from(context))
        binding.root.tag = binding
        return binding
    }
}