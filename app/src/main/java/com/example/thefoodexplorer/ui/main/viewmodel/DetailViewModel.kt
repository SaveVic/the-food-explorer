package com.example.thefoodexplorer.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thefoodexplorer.data.repositories.FoodRepository

class DetailViewModel (private val repository: FoodRepository): ViewModel(){
    private var foodID = -1
    private var detailActive = MutableLiveData<Boolean>()
    private var locationActive = MutableLiveData<Boolean>()

    init {
        detailActive.postValue(true)
        locationActive.postValue(false)
    }

    fun setFoodID(id: Int){
        foodID = id
    }

    fun setDetailUI() {
        detailActive.postValue(true)
        locationActive.postValue(false)
    }

    fun setLocationUI() {
        detailActive.postValue(false)
        locationActive.postValue(true)
    }

    fun getDetailStatus(): LiveData<Boolean> = detailActive
    fun getLocationStatus(): LiveData<Boolean> = locationActive

    fun getDetailFood() = repository.getDetailFood(foodID)
    fun getLocationFood() = repository.getLocationFood(foodID)
}