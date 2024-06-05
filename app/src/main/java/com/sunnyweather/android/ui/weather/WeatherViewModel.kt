package com.sunnyweather.android.ui.weather

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.sunnyweather.android.logic.Repository

class WeatherViewModel : ViewModel() {
    private val locationLiveData = MutableLiveData<Location>()
    var locationLng = ""
    var locationLat = ""
    var placeName = ""
    val weatherLiveData = locationLiveData.switchMap { location ->
        Repository.refreshWeather(location.longitude.toString(), location.latitude.toString())
    }
    fun refreshWeather(lng: String, lat: String) {
        val location = Location("").apply {
            longitude = lng.toDouble()
            latitude = lat.toDouble()
        }
        locationLiveData.value = location
    }
}