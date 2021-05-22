package com.practice.socialclient.ui.friends.item_fragment

import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.App
import com.practice.socialclient.ui.arch.FragmentIndication
import dagger.Module
import dagger.Provides

@Module
class ItemFriendsViewModelModule(private val fragmentType: String) {

    @Provides
    fun provideViewModelFactory(): ViewModelProvider.Factory{
        val appModule = App.appModule
        return when(fragmentType){
            FragmentIndication.FACEBOOK_INDICATION ->  ItemFriendsViewModelFactory(ItemFriendsViewModel(appModule!!.provideILog(),
                appModule.provideFacebookAuthUtilsRepository(), appModule.provideUtils(), appModule.provideFacebookFriendsRepository(),
            appModule.provideTwitterUserInfoRepository(), appModule.provideFacebookUserInfoRepository(), appModule.providePreferences()))

            FragmentIndication.TWITTER_INDICATION -> ItemFriendsViewModelFactory(ItemFriendsViewModel(appModule!!.provideILog(),
                appModule.provideTwitterAuthUtilsRepository(), appModule.provideUtils(), appModule.provideTwitterFriendsRepository(),
                appModule.provideTwitterUserInfoRepository(), appModule.provideFacebookUserInfoRepository(), appModule.providePreferences()))

            else -> throw Exception("wrong fragment indication")
        }
    }
}