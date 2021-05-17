package com.practice.socialclient.ui.login

import com.practice.socialclient.NewAppModule
import dagger.Component

@Component(dependencies = [NewAppModule::class])
interface NewLoginComponent {
    fun createLoginModel(): LoginViewModel
}