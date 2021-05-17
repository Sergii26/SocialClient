package com.practice.socialclient.ui

import com.practice.socialclient.NewAppModule
import dagger.Component

@Component(dependencies = [NewAppModule::class])
interface NewMainComponent {
    fun createMainModel(): MainActivityViewModel
}