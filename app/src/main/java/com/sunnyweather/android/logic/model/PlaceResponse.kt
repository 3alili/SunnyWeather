package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName
import java.io.Serial

//model层用来存放对象模型
//根据搜索城市数据接口返回的JSON格式定义
data class PlaceResponse(val status: String,val places: List<Place>)

//用@SerializedName让JSON字段和KOTLIN字段建立映射关系
data class Place(val name: String,val location: Location,
            @SerializedName("formatted_address") val address: String)

data class Location(val lng:String, val lat: String)