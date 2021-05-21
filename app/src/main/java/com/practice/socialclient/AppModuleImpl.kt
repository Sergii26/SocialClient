package com.practice.socialclient

import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.model.repositories.auth.AuthRepository
import com.practice.socialclient.model.repositories.auth.facebook.FacebookAuthRepository
import com.practice.socialclient.model.repositories.auth.twitter.TwitterAuthRepository
import com.practice.socialclient.model.repositories.network.facebook.FacebookApiClient
import com.practice.socialclient.model.repositories.network.facebook.client.FacebookLoginManagerImpl
import com.practice.socialclient.model.repositories.network.facebook.FacebookNetworkClient
import com.practice.socialclient.model.repositories.network.facebook.client.FacebookLoginManager
import com.practice.socialclient.model.repositories.network.twitter.TwitterApiClient
import com.practice.socialclient.model.repositories.network.twitter.TwitterNetworkClient
import com.practice.socialclient.model.repositories.network.twitter.client.TwitterClient
import com.practice.socialclient.model.repositories.network.twitter.client.TwitterClientImpl
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.model.prefs.PrefsImpl
import com.practice.socialclient.model.repositories.network.twitter.client.TwitterConstants
import com.practice.socialclient.model.repositories.friends.FriendsRepository
import com.practice.socialclient.model.repositories.friends.facebook.FacebookFriendsRepository
import com.practice.socialclient.model.repositories.friends.twitter.TwitterFriendsRepository
import com.practice.socialclient.model.repositories.news.NewsRepository
import com.practice.socialclient.model.repositories.news.facebook.FacebookNewsRepository
import com.practice.socialclient.model.repositories.news.twitter.TwitterNewsRepository
import com.practice.socialclient.model.repositories.photos.PhotosRepository
import com.practice.socialclient.model.repositories.photos.facebook.FacebookPhotosRepository
import com.practice.socialclient.model.repositories.photos.twitter.TwitterPhotosRepository
import com.practice.socialclient.model.repositories.user.UserInfoRepository
import com.practice.socialclient.model.repositories.user.facebook.FacebookUserInfoRepository
import com.practice.socialclient.model.repositories.user.twitter.TwitterUserInfoRepository
import com.practice.socialclient.model.utils_android.AndroidUtils
import com.practice.socialclient.model.utils_android.Utils
import dagger.Module
import dagger.Provides
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.conf.Configuration
import twitter4j.conf.ConfigurationBuilder
import javax.inject.Singleton


@Module
class AppModuleImpl: AppModule {
    private val facebookNetworkClient: FacebookNetworkClient
    private val prefs: Prefs
    private val twitterClient: Twitter
    private val twitterNetworkClient: TwitterNetworkClient

    init {
        facebookNetworkClient = FacebookApiClient()
        prefs = PrefsImpl(App.instance)
        val conf: Configuration = ConfigurationBuilder()
            .setDebugEnabled(true)
            .setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY)
            .setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET)
            .setIncludeEmailEnabled(true)
            .build()
        val factory = TwitterFactory(conf)
        twitterClient =  factory.instance
        twitterNetworkClient = TwitterApiClient(TwitterClientImpl(twitterClient))
    }

    @Provides
    @Singleton
    override fun provideFacebookNetworkClient(): FacebookNetworkClient {
        return facebookNetworkClient
    }

    @Provides
    override fun provideILog(): Log {
        return Logger.withTag("MyLog")
    }

    @Provides
    @Singleton
    override fun providePreferences(): Prefs {
        return prefs
    }

    @Provides
    @Singleton
    override fun provideTwitterClient(): TwitterClient {
        return TwitterClientImpl(twitterClient)
    }

    @Provides
    @Singleton
    override fun provideTwitterNetworkClient(): TwitterNetworkClient {
        return twitterNetworkClient
    }

    @Provides
    override fun provideUtils(): Utils {
        return AndroidUtils(App.instance)
    }

    @Provides
    override fun provideFacebookLoginManager(): FacebookLoginManager {
        return FacebookLoginManagerImpl(App.appModule!!.provideILog(), App.appModule!!.providePreferences())
    }

    @Provides
    override fun provideFacebookFriendsRepository(): FriendsRepository {
        return FacebookFriendsRepository(App.appModule!!.provideFacebookNetworkClient(),
            App.appModule!!.provideILog())
    }

    @Provides
    override fun provideTwitterFriendsRepository(): FriendsRepository {
        return TwitterFriendsRepository(App.appModule!!.provideTwitterNetworkClient(),
            App.appModule!!.provideILog())
    }

    @Provides
    override fun provideFacebookPhotosRepository(): PhotosRepository {
        return FacebookPhotosRepository(App.appModule!!.provideFacebookNetworkClient(),
            App.appModule!!.provideILog())
    }

    @Provides
    override fun provideTwitterPhotosRepository(): PhotosRepository {
        return TwitterPhotosRepository(App.appModule!!.provideTwitterNetworkClient(),
            App.appModule!!.provideILog())
    }

    @Provides
    override fun provideFacebookNewsRepository(): NewsRepository {
        return FacebookNewsRepository(App.appModule!!.provideFacebookNetworkClient(),
            App.appModule!!.provideILog())
    }

    @Provides
    override fun provideTwitterNewsRepository(): NewsRepository {
        return TwitterNewsRepository(App.appModule!!.provideTwitterNetworkClient(),
            App.appModule!!.provideILog())
    }

    @Provides
    override fun provideFacebookUserInfoRepository(): UserInfoRepository {
        return FacebookUserInfoRepository(App.appModule!!.provideFacebookNetworkClient(),
            App.appModule!!.provideILog())
    }

    @Provides
    override fun provideTwitterUserInfoRepository(): UserInfoRepository {
        return TwitterUserInfoRepository(App.appModule!!.provideTwitterNetworkClient(),
            App.appModule!!.provideILog())
    }

    @Provides
    override fun provideFacebookAuthRepository(): AuthRepository {
        return FacebookAuthRepository(App.appModule!!.provideILog(),
            App.appModule!!.provideTwitterClient(), App.appModule!!.providePreferences(),
            App.appModule!!.provideFacebookLoginManager())
    }

    @Provides
    override fun provideTwitterAuthRepository(): AuthRepository {
        return TwitterAuthRepository(App.appModule!!.provideILog(),
            App.appModule!!.provideTwitterClient(), App.appModule!!.providePreferences(),
            App.appModule!!.provideFacebookLoginManager())
    }





}