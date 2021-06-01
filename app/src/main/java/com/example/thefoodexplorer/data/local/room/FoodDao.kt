package com.example.thefoodexplorer.data.local.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.example.thefoodexplorer.data.local.entities.FoodEntity

@Dao
interface FoodDao {

    @Query("SELECT * FROM foodentity")
    fun getAllFood() : DataSource.Factory<Int, FoodEntity>
}