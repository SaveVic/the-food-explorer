package com.example.thefoodexplorer.data.model

import com.google.gson.annotations.SerializedName

data class FoodDetail(
    @SerializedName("description") val desc: String,
    @SerializedName("ingredient") val ingredient: List<String>,
    @SerializedName("taste") val taste: List<String>,
)
