package com.sunnyweather.android.ui.place

import android.util.Log
import android.view.animation.Transformation
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Place

class PlaceViewModel: ViewModel() {
    private val searchLiveData = MutableLiveData<String>()
    //将搜索的数据存在placeList中，保证在手机屏幕旋转时不丢失
    val placeList = ArrayList<Place>()
    val placeLiveData = searchLiveData.switchMap { query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query: String) {
        searchLiveData.value = query

        Log.d("PlaceViewModel", "searchPlaces called with query: $query")
    }
}