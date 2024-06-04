package com.sunnyweather.android.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException

object Repository {
    fun searchPlaces(query:String) = liveData(Dispatchers.IO){//指定在子线程中进行网络请求
        val result = try {
            Log.d("Repository", "searchPlaces: query = $query")
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Log.d("Repository", "searchPlaces: places = $places")
                Result.success(places)
            }else{
                Log.e("Repository", "searchPlaces: response status = ${placeResponse.status}")

                Result.failure(RuntimeException("response status is " +
                        "${placeResponse.status}"))
            }
        }catch (e: Exception){
            Log.e("Repository", "searchPlaces: exception", e)

            Result.failure<List<Place>>(e)
        }
        emit(result)
    }
}