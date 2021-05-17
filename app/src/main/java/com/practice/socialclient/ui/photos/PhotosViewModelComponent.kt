package com.practice.socialclient.ui.photos

import com.practice.socialclient.AppModule
import dagger.Component

@Component(dependencies = [AppModule::class])
interface PhotosViewModelComponent {
    fun createPhotosModel(): PhotosViewModel
}