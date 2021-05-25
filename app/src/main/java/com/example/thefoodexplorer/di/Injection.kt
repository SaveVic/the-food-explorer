package com.example.thefoodexplorer.di

import com.example.thefoodexplorer.data.api.ApiHelper
import com.example.thefoodexplorer.data.repositories.FoodRepository

object Injection {

    fun provideRepository() : FoodRepository{
        return FoodRepository.getInstance(ApiHelper.getInstance())
    }
}