package com.practice.socialclient.ui.friends

import dagger.Component

@Component(modules = [FriendsFragmentModule::class])
interface FriendsFragmentComponent {
    fun injectFriendsFragment(fragment: FriendsFragment?)
}