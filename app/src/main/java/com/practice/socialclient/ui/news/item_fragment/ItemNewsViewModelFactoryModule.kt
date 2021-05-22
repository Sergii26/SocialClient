package com.practice.socialclient.ui.news.item_fragment

import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.App
import com.practice.socialclient.ui.arch.FragmentIndication
import dagger.Module
import dagger.Provides

@Module
class ItemNewsViewModelFactoryModule(private val fragmentType: String) {

    @Provides
    fun provideItemNewsModelFactory(): ViewModelProvider.Factory{
        val appModule = App.appModule
        return when(fragmentType){
            FragmentIndication.FACEBOOK_INDICATION -> ItemNewsViewModelFactory(ItemNewsViewModel(appModule!!.provideILog(),
                    appModule.provideTwitterUserInfoRepository(), appModule.provideFacebookUserInfoRepository(), appModule.provideFacebookNewsRepository(),
                    appModule.provideFacebookAuthRepository(), appModule.provideUtils(), appModule.providePreferences()))

            FragmentIndication.TWITTER_INDICATION -> ItemNewsViewModelFactory(ItemNewsViewModel(appModule!!.provideILog(),
                    appModule.provideTwitterUserInfoRepository(), appModule.provideFacebookUserInfoRepository(), appModule.provideTwitterNewsRepository(),
                    appModule.provideTwitterAuthRepository(), appModule.provideUtils(), appModule.providePreferences()))

            else -> throw Exception("wrong fragment indication")
        }
    }

}