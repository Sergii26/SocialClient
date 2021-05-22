package com.practice.socialclient.ui.login

import com.practice.socialclient.model.AppModule
import dagger.Component

@Component(dependencies = [AppModule::class])
interface LoginViewModelComponent {
    fun createLoginModel(): LoginViewModel
}