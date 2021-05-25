package com.example.thefoodexplorer.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.example.thefoodexplorer.data.repositories.FoodRepository

class DetailViewModel (private val repository: FoodRepository): ViewModel(){
    private var foodID = -1
    fun setFoodID(id: Int){
        foodID = id
    }

    fun getDetailFood() = repository.getDetailFood(foodID)
    fun getLocationFood() = repository.getLocationFood(foodID)
}