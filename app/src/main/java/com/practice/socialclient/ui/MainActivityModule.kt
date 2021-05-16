package com.practice.socialclient.ui

import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.AppComponent
import com.practice.socialclient.model.logger.Logger
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {
    private val appComponent: AppComponent? =
        com.practice.socialclient.App.instance?.appComponent

    @Provides
    fun provideContentViewModelFactory(): ViewModelProvider.Factory {
        return MainActivityViewModelFactory(
            MainActivityViewModel(
                Logger.withTag("MyLog"), appComponent!!.provideNetworkClient(),
                appComponent.provideTwitterNetworkClient(), appComponent.providePreferences(),
                appComponent.provideTwitterClient(), appComponent.provideUtils()
            )
        )

    }

    init {
        appComponent?.injectMainActivity(this)
        if (appComponent == null) {
            Logger.withTag("MyLog").log("appComponent == null")
        } else {
            Logger.withTag("MyLog").log("appComponent != null")
        }
    }
}