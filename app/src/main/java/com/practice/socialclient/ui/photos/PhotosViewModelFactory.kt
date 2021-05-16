package com.practice.socialclient.ui.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PhotosViewModelFactory(private val photosViewModel: ViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel: ViewModel
        viewModel = if (modelClass == PhotosViewModel::class.java) {
            photosViewModel
        } else {
            throw RuntimeException("unsupported view model class: $modelClass")
        }
        return viewModel as T
    }
}