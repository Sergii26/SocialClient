package com.practice.socialclient.ui.login

import com.practice.socialclient.AppModule
import dagger.Component

@Component(dependencies = [AppModule::class])
interface LoginViewModelComponent {
    fun createLoginModel(): LoginViewModel
}