package com.example.thefoodexplorer.data.local.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foodentity")
data class FoodEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo
    var id : String,
    var name : String,
    var location : String,
    var image : String
)
