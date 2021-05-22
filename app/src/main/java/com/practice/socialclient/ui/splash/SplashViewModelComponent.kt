package com.practice.socialclient.ui.splash

import com.practice.socialclient.model.AppModule
import dagger.Component

@Component(dependencies = [AppModule::class])
interface SplashViewModelComponent {
    fun createSplashModel(): SplashViewModel
}