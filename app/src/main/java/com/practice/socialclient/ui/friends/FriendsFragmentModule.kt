//package com.practice.socialclient.ui.friends
//
//import androidx.lifecycle.ViewModelProvider
//import com.practice.socialclient.AppComponent
//import dagger.Module
//import dagger.Provides
//
//@Module
//class FriendsFragmentModule {
//    private val appComponent: AppComponent? = com.practice.socialclient.App.instance?.appComponent
//
//    @Provides
//    fun provideFriendsViewModelFactory(): ViewModelProvider.Factory {
//        return FriendsViewModelFactory(FriendsViewModel(appComponent!!.provideILog(), appComponent.provideNetworkClient(),
//            appComponent.provideTwitterNetworkClient(), appComponent.provideTwitterClient(), appComponent.providePreferences(), appComponent.provideUtils()))
//    }
//
//    init {
//        appComponent?.injectFriendsFragment(this)
//    }
//}