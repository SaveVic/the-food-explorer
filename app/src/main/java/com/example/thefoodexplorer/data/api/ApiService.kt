package com.example.thefoodexplorer.data.api

import com.example.thefoodexplorer.data.model.FoodQuery
import com.example.thefoodexplorer.data.model.BaseResponse
import com.example.thefoodexplorer.data.model.FoodDetail
import com.example.thefoodexplorer.data.model.FoodLocation
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //TODO : endpoint

    @GET("/food")
    fun getFoodByQuery(@Query("q") query: String): Call<BaseResponse<List<FoodQuery>>>

    @Multipart
    @POST("/food")
    fun getFoodByImage(@Part image: MultipartBody.Part): Call<BaseResponse<List<FoodQuery>>>

    @GET("/food/{id}/detail")
    fun getDetailFood(@Path("id") id: Int): Call<BaseResponse<FoodDetail>>

    @GET("/food/{id}/location")
    fun getLocationFood(
        @Path("id") id: Int,
//        @Query("latitude") latitude: Double,
//        @Query("longitude") longitude: Double,
    ): Call<BaseResponse<List<FoodLocation>>>

}