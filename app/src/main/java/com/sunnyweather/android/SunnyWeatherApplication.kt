package com.sunnyweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.media.session.MediaSession.Token

class SunnyWeatherApplication : Application() {

    companion object{
        const val TOKEN = "j7saodRv40v0Pvsw"
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}