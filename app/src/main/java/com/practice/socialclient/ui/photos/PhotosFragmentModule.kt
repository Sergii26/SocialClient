//package com.practice.socialclient.ui.photos
//
//import androidx.lifecycle.ViewModelProvider
//import com.practice.socialclient.AppComponent
//import dagger.Module
//import dagger.Provides
//
//@Module
//class PhotosFragmentModule {
//    private val appComponent: AppComponent? = com.practice.socialclient.App.instance?.appComponent
//    @Provides
//    fun providePhotosViewModelFactory(): ViewModelProvider.Factory {
//        return PhotosViewModelFactory(PhotosViewModel(appComponent!!.provideILog(), appComponent.provideNetworkClient(),
//        appComponent.provideTwitterNetworkClient(), appComponent.provideTwitterClient(), appComponent.providePreferences(), appComponent.provideUtils()))
//    }
//
//    init {
//        appComponent?.injectPhotosFragment(this)
//    }
//}