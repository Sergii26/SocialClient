package com.practice.socialclient.ui.photos.item_fragment

import androidx.lifecycle.ViewModelProvider
import dagger.Component

@Component(modules = [ItemPhotosViewModelFactoryModule::class])
interface ItemPhotosViewModelFactoryComponent {
    fun provideItemPhotosModelFactory(): ViewModelProvider.Factory
}