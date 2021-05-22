package com.practice.socialclient.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.App

class LoginViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass == LoginViewModel::class.java) {
            DaggerLoginViewModelComponent.builder().appModule(App.appModule).build()
                    .createLoginModel() as T
        } else {
            throw RuntimeException("unsupported view model class: $modelClass")
        }
    }
}
