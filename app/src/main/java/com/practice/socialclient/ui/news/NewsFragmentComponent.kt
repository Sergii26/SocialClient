package com.practice.socialclient.ui.news

import dagger.Component

@Component(modules = [NewsFragmentModule::class])
interface NewsFragmentComponent {
    fun injectNewsFragment(fragment: NewsFragment?)
}
