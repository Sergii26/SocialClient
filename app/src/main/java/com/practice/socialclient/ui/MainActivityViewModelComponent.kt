package com.practice.socialclient.ui

import com.practice.socialclient.AppModule
import dagger.Component

@Component(dependencies = [AppModule::class])
interface MainActivityViewModelComponent {
    fun createMainModel(): MainActivityViewModel
}