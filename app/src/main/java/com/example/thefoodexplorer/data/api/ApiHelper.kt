package com.example.thefoodexplorer.data.api

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

    suspend fun getFoodByQuery(query: String, callback: FoodQueryTextCallback) {
        ApiBuilder.service.getFoodByQuery(query).await().data.let {
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
        ApiBuilder.service.getFoodByImage(image).await().data.let {
            callback.onGetData(it)
        }
    }

    suspend fun getDetailFood(id: Int, callback: FoodDetailCallback) {
        ApiBuilder.service.getDetailFood(id).await().data.let {
            callback.onGetData(it)
        }
    }

    suspend fun getLocationFood(
        id: Int,
//        lat: Double,
//        long: Double,
        callback: FoodLocationCallback,
    ) {
        ApiBuilder.service.getLocationFood(id).await().data.let {
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


