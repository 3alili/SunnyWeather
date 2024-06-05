package com.sunnyweather.android.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {

    private val weatherService = ServiceCreator.create(WeatherService::class.java)

    suspend fun getDailyWeather(lng: String, lat: String)=
        weatherService.getDailyWeather(lng,lat).await()

    suspend fun getRealtimeWeather(lng: String, lat: String)=
        weatherService.getRealtimeWeather(lng,lat).await()

    //调用servicecreator创建一个PlaceService接口的动态代理对象
    private val placeService = ServiceCreator.create<PlaceService>()

    //调用PlaceService的searchPlaces方法，发起搜索城市数据请求
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    //await 函数的作用是将 Retrofit 的异步回调接口转换为同步的挂起函数，
    //使得可以在协程中使用同步代码风格来处理网络请求。
    //lambda表达式会在普通线程中立刻执行，而当前协程被立刻挂起。当请求成功或失败后协程才恢复运行,
    //这样即使不用回调的写法，也能获得异步网络请求的响应数据。
    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}