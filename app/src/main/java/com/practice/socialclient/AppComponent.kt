package com.practice.socialclient


import com.practice.socialclient.model.logger.ILog
import com.practice.socialclient.model.network_api.facebook.FacebookNetworkClient
import com.practice.socialclient.model.network_api.twitter.TwitterNetworkClient
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.model.utils.Utils
import com.practice.socialclient.ui.MainActivityModule
import com.practice.socialclient.ui.friends.FriendsFragmentModule
import com.practice.socialclient.ui.login.LoginFragmentModule
import com.practice.socialclient.ui.news.NewsFragmentModule
import com.practice.socialclient.ui.photos.PhotosFragmentModule
import com.practice.socialclient.ui.splash.SplashFragmentModule
import dagger.Component
import twitter4j.Twitter
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun injectSplashFragment(module: SplashFragmentModule)
    fun injectLoginFragment(module: LoginFragmentModule)
    fun injectMainActivity(module: MainActivityModule)
    fun injectFriendsFragment(module: FriendsFragmentModule)
    fun injectPhotosFragment(module: PhotosFragmentModule)
    fun injectNewsFragment(module: NewsFragmentModule)
    fun provideNetworkClient(): FacebookNetworkClient
    fun provideILog(): ILog
    fun providePreferences(): Prefs
    fun provideTwitterClient(): Twitter
    fun provideTwitterNetworkClient(): TwitterNetworkClient
    fun provideUtils(): Utils
}