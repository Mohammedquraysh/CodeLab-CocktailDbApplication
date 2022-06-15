package com.example.codelabapplication.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class CocktailApplication: Application() {
    companion object {
        @get:Synchronized
        lateinit var application: CocktailApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }

}