package com.practice.socialclient.ui.news.item_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.model.logger.Logger


class ItemNewsViewModelFactory(private val itemNewsViewModel: ViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        Logger.withTag("MyLog").log("PhotosViewModelFactory")
        if (modelClass == ItemNewsViewModel::class.java) {
            Logger.withTag("MyLog").log("PhotosViewModelFactory return factory")
            return itemNewsViewModel as T
        } else {
            throw RuntimeException("unsupported view model class: $modelClass")
        }
    }
}