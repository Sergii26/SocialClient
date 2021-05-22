package com.practice.socialclient.model

import com.practice.socialclient.App
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.model.prefs.PrefsImpl
import com.practice.socialclient.model.repositories.auth.facebook.FacebookAuthRepository
import com.practice.socialclient.model.repositories.auth.facebook.FacebookAuthRepositoryImpl
import com.practice.socialclient.model.repositories.auth.twitter.TwitterAuthRepository
import com.practice.socialclient.model.repositories.auth.twitter.TwitterAuthRepositoryImpl
import com.practice.socialclient.model.repositories.auth.twitter.TwitterConstants
import com.practice.socialclient.model.repositories.friends.FriendsRepository
import com.practice.socialclient.model.repositories.friends.facebook.FacebookFriendsRepository
import com.practice.socialclient.model.repositories.friends.twitter.TwitterFriendsRepository
import com.practice.socialclient.model.repositories.network.facebook.FacebookApiClient
import com.practice.socialclient.model.repositories.network.facebook.FacebookNetworkClient
import com.practice.socialclient.model.repositories.network.twitter.TwitterApiClient
import com.practice.socialclient.model.repositories.network.twitter.TwitterNetworkClient
import com.practice.socialclient.model.repositories.network.twitter.header_factory.TwitterAuthHeaderFactory
import com.practice.socialclient.model.repositories.network.twitter.header_factory.TwitterHeaderFactory
import com.practice.socialclient.model.repositories.news.NewsRepository
import com.practice.socialclient.model.repositories.news.facebook.FacebookNewsRepository
import com.practice.socialclient.model.repositories.news.twitter.TwitterNewsRepository
import com.practice.socialclient.model.repositories.photos.PhotosRepository
import com.practice.socialclient.model.repositories.photos.facebook.FacebookPhotosRepository
import com.practice.socialclient.model.repositories.photos.twitter.TwitterPhotosRepository
import com.practice.socialclient.model.repositories.user.UserInfoRepository
import com.practice.socialclient.model.repositories.user.facebook.FacebookUserInfoRepository
import com.practice.socialclient.model.repositories.user.twitter.TwitterUserInfoRepository
import com.practice.socialclient.model.utils.AndroidUtils
import com.practice.socialclient.model.utils.Utils
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
        twitterNetworkClient = TwitterApiClient(TwitterAuthHeaderFactory(twitterClient))
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
    override fun provideTwitterHeaderFactory(): TwitterHeaderFactory {
        return TwitterAuthHeaderFactory(twitterClient)
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
    override fun provideFacebookFriendsRepository(): FriendsRepository {
        return FacebookFriendsRepository(provideFacebookNetworkClient(), provideILog())
    }

    @Provides
    override fun provideTwitterFriendsRepository(): FriendsRepository {
        return TwitterFriendsRepository(provideTwitterNetworkClient(), provideILog())
    }

    @Provides
    override fun provideFacebookPhotosRepository(): PhotosRepository {
        return FacebookPhotosRepository(provideFacebookNetworkClient(), provideILog())
    }

    @Provides
    override fun provideTwitterPhotosRepository(): PhotosRepository {
        return TwitterPhotosRepository(provideTwitterNetworkClient(), provideILog())
    }

    @Provides
    override fun provideFacebookNewsRepository(): NewsRepository {
        return FacebookNewsRepository(provideFacebookNetworkClient(), provideILog())
    }

    @Provides
    override fun provideTwitterNewsRepository(): NewsRepository {
        return TwitterNewsRepository(provideTwitterNetworkClient(), provideILog())
    }

    @Provides
    override fun provideFacebookUserInfoRepository(): UserInfoRepository {
        return FacebookUserInfoRepository(provideFacebookNetworkClient(), provideILog())
    }

    @Provides
    override fun provideTwitterUserInfoRepository(): UserInfoRepository {
        return TwitterUserInfoRepository(provideTwitterNetworkClient(), provideILog())
    }


    @Provides
    override fun provideFacebookAuthRepository(): FacebookAuthRepository {
        return FacebookAuthRepositoryImpl(provideILog(), providePreferences())
    }

    @Provides
    override fun provideTwitterAuthRepository(): TwitterAuthRepository {
        return TwitterAuthRepositoryImpl(twitterClient, provideTwitterNetworkClient(), providePreferences())
    }
}
