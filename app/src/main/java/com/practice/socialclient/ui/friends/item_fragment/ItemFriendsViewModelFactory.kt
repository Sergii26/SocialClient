package com.practice.socialclient.ui.friends.item_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ItemFriendsViewModelFactory(private val itemFriendsViewModel: ViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == ItemFriendsViewModel::class.java) {
            return itemFriendsViewModel as T
        } else {
            throw RuntimeException("unsupported view model class: $modelClass")
        }
    }
}