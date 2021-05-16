package com.practice.socialclient

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.practice.socialclient.model.logger.Logger
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class App : MultiDexApplication(){
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