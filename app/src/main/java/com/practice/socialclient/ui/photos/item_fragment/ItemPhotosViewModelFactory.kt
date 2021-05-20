package com.practice.socialclient.ui.photos.item_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.model.logger.Logger


class ItemPhotosViewModelFactory(private val itemPhotosViewModel: ViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        Logger.withTag("MyLog").log("PhotosViewModelFactory")
        if (modelClass == ItemPhotosViewModel::class.java) {
            Logger.withTag("MyLog").log("PhotosViewModelFactory return factory")
            return itemPhotosViewModel as T
        } else {
            throw RuntimeException("unsupported view model class: $modelClass")
        }
    }
}