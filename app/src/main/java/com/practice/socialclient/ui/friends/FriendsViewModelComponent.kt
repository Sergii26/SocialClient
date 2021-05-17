package com.practice.socialclient.ui.friends

import com.practice.socialclient.AppModule
import dagger.Component

@Component(dependencies = [AppModule::class])
interface FriendsViewModelComponent {
    fun createFriendsModel(): FriendsViewModel
}