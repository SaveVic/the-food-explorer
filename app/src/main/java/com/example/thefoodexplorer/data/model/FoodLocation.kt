package com.example.thefoodexplorer.data.model

import com.google.gson.annotations.SerializedName

data class FoodLocation(
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String,
//    @SerializedName("latitude") val lat: Double,
//    @SerializedName("longitude") val long: Double,
    @SerializedName("map_url") val url: String,
)
