package com.example.thefoodexplorer.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.thefoodexplorer.data.api.ApiHelper
import com.example.thefoodexplorer.data.api.FoodQueryImageCallback
import com.example.thefoodexplorer.data.api.FoodQueryTextCallback
import com.example.thefoodexplorer.data.model.FoodQuery
import com.example.thefoodexplorer.util.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

class FoodRepository private constructor(private val helper: ApiHelper){
    companion object {
        @Volatile
        private var instance: FoodRepository? = null

        fun getInstance(helper: ApiHelper): FoodRepository =
            instance ?: synchronized(this) {
                instance ?: FoodRepository(helper)
            }
    }

    fun getFoodByQuery(query: String) : LiveData<ApiResponse<List<FoodQuery>>>{
        val liveData = MutableLiveData<ApiResponse<List<FoodQuery>>>()
        liveData.postValue(ApiResponse.loading())
        CoroutineScope(IO).launch {
            try {
                helper.getFoodByQuery(query, object : FoodQueryTextCallback{
                    override fun onGetData(data: List<FoodQuery>) {
                        liveData.postValue(ApiResponse.success(data))
                    }
                })
            }catch (e: Exception){
                liveData.postValue(ApiResponse.error(e.toString()))
            }

        }
        return liveData
    }

    fun getFoodByImage(image: File) : LiveData<ApiResponse<List<FoodQuery>>>{
        val liveData = MutableLiveData<ApiResponse<List<FoodQuery>>>()
        liveData.postValue(ApiResponse.loading())
        CoroutineScope(IO).launch {
            try {
                helper.getFoodByImage(image, object : FoodQueryImageCallback{
                    override fun onGetData(data: List<FoodQuery>) {
                        liveData.postValue(ApiResponse.success(data))
                    }
                })
            }catch (e: Exception){
                liveData.postValue(ApiResponse.error(e.toString()))
            }

        }
        return liveData
    }
}