package com.practice.socialclient.ui.friends

import com.practice.socialclient.NewAppModule
import dagger.Component

@Component(dependencies = [NewAppModule::class])
interface NewFriendsComponent {
    fun createFriendsModel(): FriendsViewModel
}