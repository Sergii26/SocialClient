package com.practice.socialclient.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.App


class NewsViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel: ViewModel
        viewModel = if (modelClass == NewsViewModel::class.java) {

            DaggerNewsViewModelComponent.builder().appModule(App.appModule).build()
                .createNewsModel()
        } else {
            throw RuntimeException("unsupported view model class: $modelClass")
        }
        return viewModel as T
    }
}