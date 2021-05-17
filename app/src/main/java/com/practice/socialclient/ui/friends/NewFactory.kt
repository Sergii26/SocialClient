package com.practice.socialclient.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.App


class NewFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel: ViewModel
        viewModel = if (modelClass == FriendsViewModel::class.java) {

            DaggerNewFriendsComponent.builder()
                .newAppModule(App.appModule)
                .build()
                .createFriendsModel()

        } else {
            throw RuntimeException("unsupported view model class: $modelClass")
        }
        return viewModel as T
    }
}