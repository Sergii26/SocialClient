package com.practice.socialclient

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.practice.socialclient.model.AppModule
import com.practice.socialclient.model.AppModuleImpl


class App : MultiDexApplication() {

    val appContext: Context
        get() = this.applicationContext

    override fun onCreate() {
        super.onCreate()
        instance = this

        appModule = AppModuleImpl()
    }

    companion object {
        var instance: App? = null
            private set
        var appModule: AppModule? = null
    }
}
