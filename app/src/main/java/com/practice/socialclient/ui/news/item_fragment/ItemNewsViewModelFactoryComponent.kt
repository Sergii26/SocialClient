package com.practice.socialclient.ui.news.item_fragment

import androidx.lifecycle.ViewModelProvider
import dagger.Component

@Component(modules = [ItemNewsViewModelFactoryModule::class])
interface ItemNewsViewModelFactoryComponent {
    fun provideItemNewsViewModelFactory(): ViewModelProvider.Factory
}