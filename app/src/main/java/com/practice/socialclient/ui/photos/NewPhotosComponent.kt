package com.practice.socialclient.ui.photos

import com.practice.socialclient.NewAppModule
import dagger.Component

@Component(dependencies = [NewAppModule::class])
interface NewPhotosComponent {
    fun createPhotosModel(): PhotosViewModel
}