package com.example.thefoodexplorer.ui.main.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thefoodexplorer.data.model.FoodQuery
import com.example.thefoodexplorer.databinding.ItemCategoryBinding
import com.example.thefoodexplorer.ui.main.view.FoodDetailActivity
import java.util.*

class FoodListAdapter : RecyclerView.Adapter<FoodListAdapter.ViewHolder>(){

//    : PagedListAdapter<FoodEntity, FoodListAdapter.ViewHolder>(DIFF_CALLBACK){
//
//
//    companion object{
//        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FoodEntity>() {
//            override fun areItemsTheSame(oldItem: FoodEntity, newItem: FoodEntity): Boolean {
//                return oldItem.id == newItem.id
//            }
//
//            override fun areContentsTheSame(oldItem: FoodEntity, newItem: FoodEntity): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }

    private val food = ArrayList<FoodQuery>()

    fun setFood(mFood: List<FoodQuery>?){
        if (mFood == null) return
        this.food.clear()
        this.food.addAll(mFood)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val food = getItem(position)
//        if (food != null){
//            holder.bind(food)
//        }
        holder.bind(food[position])
    }


    override fun getItemCount(): Int = food.size

    class ViewHolder(private val binding : ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(food: FoodEntity) {
//            binding.apply {
//                name.text = food.name
//                Glide.with(itemView.context)
//                    .load(food.image)
//                    .into(image)
//            }

        fun bind(food: FoodQuery) {
            binding.apply {
                name.text = food.name
                city.text = food.city
                Glide.with(itemView.context)
                    .load(food.image)
                    .into(image)

                itemView.setOnClickListener{
                    val intent = Intent(itemView.context, FoodDetailActivity::class.java)
                    intent.putExtra(FoodDetailActivity.DATA, food)
                    itemView.context.startActivity(intent)
                }
            }

        }

    }


}