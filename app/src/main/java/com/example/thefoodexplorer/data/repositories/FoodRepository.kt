package com.example.thefoodexplorer.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.thefoodexplorer.data.api.*
import com.example.thefoodexplorer.data.dummy.DummyData
import com.example.thefoodexplorer.data.model.FoodDetail
import com.example.thefoodexplorer.data.model.FoodLocation
import com.example.thefoodexplorer.data.model.FoodQuery
import com.example.thefoodexplorer.util.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class FoodRepository private constructor(private val helper: ApiHelper,
//                                         private val localDataSource: LocalDataSource, private val appExecutors: AppExecutors
                                         ){

    companion object {
        @Volatile
        private var instance: FoodRepository? = null

        fun getInstance(helper: ApiHelper): FoodRepository =
            instance ?: synchronized(this) {
                instance ?: FoodRepository(helper)
            }
    }

//    fun getAllFood():LiveData<Resource<PagedList<FoodEntity>>>{
//
//    }

    fun getAllFood(): LiveData<ApiResponse<List<FoodQuery>>> {
        val liveData = MutableLiveData<ApiResponse<List<FoodQuery>>>()
        liveData.postValue(ApiResponse.loading())
        CoroutineScope(IO).launch {
//            delay(1000L)
            try {
                helper.getAllFood(object : FoodQueryTextCallback{
                    override fun onGetData(data: List<FoodQuery>) {
                        liveData.postValue(ApiResponse.success(data))
                    }
                })
            }catch (e: Exception){
                Log.e("ERROR", e.toString())
                liveData.postValue(ApiResponse.error(e.toString()))
//                liveData.postValue(ApiResponse.success(DummyData.getFoods()))
            }

        }
        return liveData
    }

    fun getFoodByQuery(query: String) : LiveData<ApiResponse<List<FoodQuery>>>{
        val liveData = MutableLiveData<ApiResponse<List<FoodQuery>>>()
        liveData.postValue(ApiResponse.loading())
        CoroutineScope(IO).launch {
//            delay(1000L)
            try {
                helper.getFoodByQuery(query, object : FoodQueryTextCallback{
                    override fun onGetData(data: List<FoodQuery>) {
                        liveData.postValue(ApiResponse.success(data))
                    }
                })
            }catch (e: Exception){
                Log.e("Error", e.toString())
                liveData.postValue(ApiResponse.error(e.toString()))
//                liveData.postValue(ApiResponse.success(DummyData.getFoods()))
            }

        }
        return liveData
    }

    fun getFoodByImage(image: File) : LiveData<ApiResponse<List<FoodQuery>>>{
        val liveData = MutableLiveData<ApiResponse<List<FoodQuery>>>()
        liveData.postValue(ApiResponse.loading())
        CoroutineScope(IO).launch {
//            delay(1000L)
            try {
                helper.getFoodByImage(image, object : FoodQueryImageCallback{
                    override fun onGetData(data: List<FoodQuery>) {
                        liveData.postValue(ApiResponse.success(data))
                    }
                })
            }catch (e: Exception){
                Log.e("ERROR", e.toString())
                liveData.postValue(ApiResponse.error(e.toString()))
//                liveData.postValue(ApiResponse.success(DummyData.getFoods()))
            }

        }
        return liveData
    }

    fun getDetailFood(id: Int) : LiveData<ApiResponse<FoodDetail>>{
        val liveData = MutableLiveData<ApiResponse<FoodDetail>>()
        liveData.postValue(ApiResponse.loading())
        CoroutineScope(IO).launch {
            delay(2000L)
            try {
                helper.getDetailFood(id, object : FoodDetailCallback{
                    override fun onGetData(data: FoodDetail) {
                        liveData.postValue(ApiResponse.success(data))
                    }
                })
            }catch (e: Exception){
                Log.e("Error", e.toString())
                liveData.postValue(ApiResponse.error(e.toString()))
//                liveData.postValue(ApiResponse.success(DummyData.getDetailFood()))
            }

        }
        return liveData
    }

    fun getLocationFood(id: Int,
//                        lat: Double,
//                        long: Double
    ) : LiveData<ApiResponse<List<FoodLocation>>>{
        val liveData = MutableLiveData<ApiResponse<List<FoodLocation>>>()
        liveData.postValue(ApiResponse.loading())
        CoroutineScope(IO).launch {
//            delay(1000L)
            try {
                helper.getLocationFood(id,
//                    lat,
//                    long,
                    object : FoodLocationCallback{
                    override fun onGetData(data: List<FoodLocation>) {
                        liveData.postValue(ApiResponse.success(data))
                    }
                })
            }catch (e: Exception){
                Log.e("Error", e.toString())
//                liveData.postValue(ApiResponse.error(e.toString()))
                liveData.postValue(ApiResponse.success(DummyData.getLocationFood()))
            }

        }
        return liveData
    }

}