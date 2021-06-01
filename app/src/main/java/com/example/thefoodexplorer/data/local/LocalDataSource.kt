package com.example.thefoodexplorer.data.local

import androidx.paging.DataSource
import com.example.thefoodexplorer.data.local.entities.FoodEntity
import com.example.thefoodexplorer.data.local.room.FoodDao

class LocalDataSource(private val mFoodDao: FoodDao) {

    companion object{
        private var Instance : LocalDataSource? = null

        fun getInstance(foodDao: FoodDao): LocalDataSource =
            Instance ?: LocalDataSource(foodDao).apply { Instance = this }
    }

    fun getAllFood() : DataSource.Factory<Int, FoodEntity> = mFoodDao.getAllFood()
}