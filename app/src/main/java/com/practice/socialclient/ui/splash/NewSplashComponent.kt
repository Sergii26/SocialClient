package com.practice.socialclient.ui.splash

import com.practice.socialclient.NewAppModule
import dagger.Component

@Component(dependencies = [NewAppModule::class])
interface NewSplashComponent {
    fun createSplashModel(): SplashViewModel
}