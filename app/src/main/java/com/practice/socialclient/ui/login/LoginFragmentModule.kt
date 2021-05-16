package com.practice.socialclient.ui.login

import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.AppComponent
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.ui.MainActivityViewModel
import com.practice.socialclient.ui.MainActivityViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class LoginFragmentModule {
    private val appComponent: AppComponent = com.practice.socialclient.App.instance?.appComponent!!

    @Provides
    fun provideSplashViewModelFactory(): ViewModelProvider.Factory {

//        val token = appComponent.providePreferences().getTwitterAuthToken()
//        val secret = appComponent.providePreferences().getTwitterAuthSecret()

        return LoginViewModelFactory(LoginViewModel(Logger.withTag("MyLog"), appComponent.providePreferences(),
            appComponent.provideTwitterClient(),
            appComponent.provideTwitterNetworkClient()))
    }

    init {
        appComponent.injectLoginFragment(this)
    }
}