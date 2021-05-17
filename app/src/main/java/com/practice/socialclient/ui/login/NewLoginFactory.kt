package com.practice.socialclient.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.App


class NewLoginFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel: ViewModel
        viewModel = if (modelClass == LoginViewModel::class.java) {

            DaggerNewLoginComponent.builder().newAppModule(App.appModule).build()
                .createLoginModel()
        } else {
            throw RuntimeException("unsupported view model class: $modelClass")
        }
        return viewModel as T
    }
}