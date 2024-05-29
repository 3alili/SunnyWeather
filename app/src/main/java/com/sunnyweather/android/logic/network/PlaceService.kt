package com.sunnyweather.android.logic.network

import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    //@GET注解中配置访问的地址，以及另外两个固定参数：token和lang
    //@Query注解中配置动态参数query,方法将JSON解析成PlaceResponse类对象返回
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh-CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}