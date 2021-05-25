package com.example.thefoodexplorer.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.thefoodexplorer.data.model.FoodLocation
import com.example.thefoodexplorer.data.model.FoodQuery
import com.example.thefoodexplorer.databinding.ItemLocationBinding
import com.example.thefoodexplorer.databinding.ItemSearchResultBinding

class FoodLocationGridAdapter(private val data: ArrayList<FoodLocation>
) : RecyclerView.Adapter<FoodLocationGridAdapter.ViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(location: FoodLocation)
    }

    inner class ViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(location: FoodLocation) {
            with(binding) {
                name.text = location.name
                address.text = location.address
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(location) }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val binding =
            ItemLocationBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun replaceList(newData: List<FoodLocation>) {
        data.clear()
        data.addAll(newData)
    }
}