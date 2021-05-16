package com.practice.socialclient.ui.photos

import dagger.Component

@Component(modules = [PhotosFragmentModule::class])
interface PhotosFragmentComponent {
    fun injectPhotosFragment(fragment: PhotosFragment?)
}
