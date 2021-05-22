package com.practice.socialclient.ui.photos.item_fragment

import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.App
import com.practice.socialclient.ui.arch.FragmentIndication
import dagger.Module
import dagger.Provides

@Module
class ItemPhotosViewModelFactoryModule(private val fragmentType: String) {

    @Provides
    fun provideItemPhotosModelFactory(): ViewModelProvider.Factory{
        val appModule = App.appModule
        return when(fragmentType){
            FragmentIndication.FACEBOOK_INDICATION -> ItemPhotosViewModelFactory(ItemPhotosViewModel(appModule!!.provideILog(),
                    appModule.provideFacebookAuthRepository(), appModule.provideUtils(), appModule.provideFacebookPhotosRepository(),
                    appModule.provideTwitterUserInfoRepository(), appModule.provideFacebookUserInfoRepository(), appModule.providePreferences()))

            FragmentIndication.TWITTER_INDICATION -> ItemPhotosViewModelFactory(ItemPhotosViewModel(appModule!!.provideILog(),
                    appModule.provideTwitterAuthRepository(), appModule.provideUtils(), appModule.provideTwitterPhotosRepository(),
                    appModule.provideTwitterUserInfoRepository(), appModule.provideFacebookUserInfoRepository(), appModule.providePreferences()))

            else -> throw Exception("wrong fragment indication")
        }
    }
}