package com.practice.socialclient.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FriendsViewModelFactory(private val friendsViewModel: ViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel: ViewModel
        viewModel = if (modelClass == FriendsViewModel::class.java) {
            friendsViewModel
        } else {
            throw RuntimeException("unsupported view model class: $modelClass")
        }
        return viewModel as T
    }

}