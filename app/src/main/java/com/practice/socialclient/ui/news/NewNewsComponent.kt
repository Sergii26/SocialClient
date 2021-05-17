package com.practice.socialclient.ui.news

import com.practice.socialclient.NewAppModule
import dagger.Component

@Component(dependencies = [NewAppModule::class])
interface NewNewsComponent {
    fun createNewsModel(): NewsViewModel
}