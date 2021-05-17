package com.practice.socialclient.ui.news

import com.practice.socialclient.AppModule
import dagger.Component

@Component(dependencies = [AppModule::class])
interface NewsViewModelComponent {
    fun createNewsModel(): NewsViewModel
}