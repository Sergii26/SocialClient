package com.practice.socialclient.ui.friends.item_fragment

import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.App
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.ui.arch.FragmentIndication
import dagger.Module
import dagger.Provides
import java.lang.Exception

@Module
class ItemFriendsViewModelModule(private val fragmentType: String) {

    @Provides
    fun provideViewModelFactory(): ViewModelProvider.Factory{
        val appModule = App.appModule
        return when(fragmentType){
            FragmentIndication.FACEBOOK_INDICATION ->  ItemFriendsViewModelFactory(ItemFriendsViewModel(appModule!!.provideILog(),
                appModule.provideFacebookStateLoginUtil(), appModule.provideUtils(), appModule.provideFacebookFriendsRepository(),
            appModule.provideTwitterUserInfoRepository(), appModule.provideFacebookUserInfoRepository(), appModule.providePreferences(), appModule.provideLogOutUtil()))

            FragmentIndication.TWITTER_INDICATION -> ItemFriendsViewModelFactory(ItemFriendsViewModel(appModule!!.provideILog(),
                appModule.provideTwitterStateLoginUtil(), appModule.provideUtils(), appModule.provideTwitterFriendsRepository(),
                appModule.provideTwitterUserInfoRepository(), appModule.provideFacebookUserInfoRepository(), appModule.providePreferences(), appModule.provideLogOutUtil()))

            else -> throw Exception("wrong fragment indication")
        }
    }
}