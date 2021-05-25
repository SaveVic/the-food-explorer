package com.example.thefoodexplorer.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FoodQuery(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("city") val city: String,
    @SerializedName("image") val image: String,
): Serializable
