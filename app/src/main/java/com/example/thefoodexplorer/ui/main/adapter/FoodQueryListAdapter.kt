package com.example.thefoodexplorer.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.thefoodexplorer.data.model.FoodQuery
import com.example.thefoodexplorer.databinding.ItemCategoryBinding
import com.example.thefoodexplorer.databinding.ItemSearchResultBinding

class FoodQueryListAdapter(private val data: ArrayList<FoodQuery>,
                           private val isClickable: Boolean = true
) : RecyclerView.Adapter<FoodQueryListAdapter.ViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(food: FoodQuery)
    }

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(food: FoodQuery) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(food.image)
                    .into(image)
                name.text = food.name
                city.text = food.city
                if (isClickable) {
                    itemView.setOnClickListener { onItemClickCallback?.onItemClicked(food) }
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun replaceList(newData: List<FoodQuery>) {
        data.clear()
        data.addAll(newData)
    }
}