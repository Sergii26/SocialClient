package com.practice.socialclient

import android.content.Context
import androidx.multidex.MultiDexApplication

class App : MultiDexApplication() {
    var appComponent: AppComponent? = null
    val appContext: Context
        get() = this.applicationContext

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.create()
    }

    companion object {
        var instance: App? = null
            private set
    }
}
