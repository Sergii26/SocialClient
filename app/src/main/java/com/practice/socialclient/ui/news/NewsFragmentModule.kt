package com.practice.socialclient.ui.news

import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.AppComponent
import dagger.Module
import dagger.Provides

@Module
class NewsFragmentModule {
    private val appComponent: AppComponent? = com.practice.socialclient.App.instance?.appComponent
    @Provides
    fun provideNewsViewModelFactory(): ViewModelProvider.Factory {
//        val token = appComponent?.providePreferences()?.getTwitterAuthToken().toString()
//        val secret = appComponent?.providePreferences()?.getTwitterAuthSecret().toString()
        return NewsViewModelFactory(NewsViewModel(appComponent!!.provideILog(), appComponent.provideNetworkClient(),
            appComponent.provideTwitterNetworkClient(), appComponent.provideTwitterClient(), appComponent.providePreferences(), appComponent.provideUtils()))
    }

    init {
        appComponent?.injectNewsFragment(this)
    }
}