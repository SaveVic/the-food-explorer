package com.example.thefoodexplorer.data.api

import android.util.Log
import com.example.thefoodexplorer.data.model.FoodDetail
import com.example.thefoodexplorer.data.model.FoodLocation
import com.example.thefoodexplorer.data.model.FoodQuery
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.await
import java.io.File

class ApiHelper {
    companion object {
        @Volatile
        private var instance: ApiHelper? = null

        fun getInstance(): ApiHelper =
            instance ?: synchronized(this) {
                instance ?: ApiHelper()
            }
    }

    suspend fun getAllFood(callback: FoodQueryTextCallback) {
        val response = ApiBuilder.service.getAllFood().await()
        Log.e("MESSAGE", response.msg)
        response.data.let {
            callback.onGetData(it)
        }
    }

    suspend fun getFoodByQuery(query: String, callback: FoodQueryTextCallback) {
        val response = ApiBuilder.service.getFoodByQuery(query).await()
        Log.e("MESSAGE", response.msg)
        response.data.let {
            callback.onGetData(it)
        }
    }

    suspend fun getFoodByImage(file: File, callback: FoodQueryImageCallback) {
        val requestFile = RequestBody.create(
            MediaType.parse("jpg"),
            file
        )
        val image = MultipartBody.Part
            .createFormData("image", file.name, requestFile)
        val response = ApiBuilder.service.getFoodByImage(image).await()
        Log.e("MESSAGE", response.msg)
        response.data.let {
            callback.onGetData(it)
        }
    }

    suspend fun getDetailFood(id: Int, callback: FoodDetailCallback) {
        val response = ApiBuilder.service.getDetailFood(id).await()
        Log.e("MESSAGE", response.msg)
        response.data.let {
            callback.onGetData(it)
        }
    }

    suspend fun getLocationFood(
        id: Int,
//        lat: Double,
//        long: Double,
        callback: FoodLocationCallback,
    ) {
        val response = ApiBuilder.service.getLocationFood(id).await()
        Log.e("MESSAGE", response.msg)
        response.data.let {
            callback.onGetData(it)
        }
    }
}

interface FoodQueryImageCallback {
    fun onGetData(data: List<FoodQuery>)
}

interface FoodQueryTextCallback {
    fun onGetData(data: List<FoodQuery>)
}

interface FoodDetailCallback {
    fun onGetData(data: FoodDetail)
}

interface FoodLocationCallback {
    fun onGetData(data: List<FoodLocation>)
}


