package com.example.studysmart.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}