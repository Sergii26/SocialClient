package com.practice.socialclient.ui.news.item_fragment

import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.AppModule
import dagger.Component

@Component(modules = [ItemNewsViewModelFactoryModule::class])
interface ItemNewsViewModelFactoryComponent {
    fun provideItemNewsViewModelFactory(): ViewModelProvider.Factory
}