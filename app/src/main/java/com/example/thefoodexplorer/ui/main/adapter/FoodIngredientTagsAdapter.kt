package com.example.thefoodexplorer.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.thefoodexplorer.databinding.ItemTagIngredientBinding

class FoodIngredientTagsAdapter(private val data: ArrayList<String>
) : RecyclerView.Adapter<FoodIngredientTagsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemTagIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tagName: String) {
            binding.tag.text = tagName
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val binding =
            ItemTagIngredientBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun replaceList(newData: List<String>) {
        data.clear()
        data.addAll(newData)
    }
}