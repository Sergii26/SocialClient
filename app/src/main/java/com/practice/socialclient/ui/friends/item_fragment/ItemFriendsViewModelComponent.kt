package com.practice.socialclient.ui.friends.item_fragment


import androidx.lifecycle.ViewModelProvider
import dagger.Component


@Component(modules = [ItemFriendsViewModelModule::class])
interface ItemFriendsViewModelComponent {
    fun provideItemFriendsModelFactory(): ViewModelProvider.Factory
}