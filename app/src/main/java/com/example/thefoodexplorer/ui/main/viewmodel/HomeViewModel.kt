package com.example.thefoodexplorer.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thefoodexplorer.data.model.FoodQuery
import com.example.thefoodexplorer.data.repositories.FoodRepository
import com.example.thefoodexplorer.util.ApiResponse
import com.example.thefoodexplorer.util.RefreshableLiveData
import java.io.File

class HomeViewModel (private val repository: FoodRepository): ViewModel(){
    private val _allFoods = RefreshableLiveData {
        repository.getAllFood()
    }

    val allFoods get() = _allFoods
    fun getFoodByQuery(query: String) = repository.getFoodByQuery(query)
    fun getFoodByImage(image: File) = repository.getFoodByImage(image)

    fun refreshAllFoods() {
        _allFoods.refresh()
    }
}