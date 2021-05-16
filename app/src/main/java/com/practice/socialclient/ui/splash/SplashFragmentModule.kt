package com.practice.socialclient.ui.splash

import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.AppComponent
import com.practice.socialclient.model.logger.Logger
import dagger.Module
import dagger.Provides

@Module
class SplashFragmentModule {
    private val appComponent: AppComponent? = com.practice.socialclient.App.instance?.appComponent

    @Provides
    fun provideSplashViewModelFactory(): ViewModelProvider.Factory {
        return SplashViewModelFactory(
            SplashViewModel(
                Logger.withTag("MyLog"),
                appComponent!!.provideTwitterClient(),
                appComponent.providePreferences(),
                appComponent.provideTwitterNetworkClient()
            )
        )
    }

    init {
        appComponent?.injectSplashFragment(this)
    }
}