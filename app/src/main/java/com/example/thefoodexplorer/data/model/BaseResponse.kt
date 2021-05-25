package com.example.thefoodexplorer.data.model

import com.google.gson.annotations.SerializedName

data class BaseResponse<out T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val msg: String,
    @SerializedName("data") val data: T,
)