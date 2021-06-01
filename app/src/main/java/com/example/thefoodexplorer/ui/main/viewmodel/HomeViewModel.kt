package com.example.thefoodexplorer.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.example.thefoodexplorer.data.repositories.FoodRepository
import java.io.File

class HomeViewModel (private val repository: FoodRepository): ViewModel(){
    fun getAllFood() = repository.getAllFood()
    fun getFoodByQuery(query: String) = repository.getFoodByQuery(query)
    fun getFoodByImage(image: File) = repository.getFoodByImage(image)
}