package com.practice.socialclient.ui.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.App


class PhotosViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel: ViewModel
        viewModel = if (modelClass == PhotosViewModel::class.java) {

            DaggerPhotosViewModelComponent.builder().appModule(App.appModule).build()
                .createPhotosModel()
        } else {
            throw RuntimeException("unsupported view model class: $modelClass")
        }
        return viewModel as T
    }
}