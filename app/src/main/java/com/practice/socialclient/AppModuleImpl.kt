package com.practice.socialclient

import com.practice.socialclient.model.logger.ILog
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.model.network_api.facebook.FacebookApiClient
import com.practice.socialclient.model.network_api.facebook.FacebookNetworkClient
import com.practice.socialclient.model.network_api.twitter.TwitterApiClient
import com.practice.socialclient.model.network_api.twitter.TwitterNetworkClient
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.model.prefs.PrefsImpl
import com.practice.socialclient.model.twitter.TwitterConstants
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
class AppModuleImpl(): NewAppModule {
    @Provides
    @Singleton
    override fun provideNetworkClient(): FacebookNetworkClient {
        return FacebookApiClient()
    }

    @Provides
    override fun provideILog(): ILog {
        return Logger.withTag("MyLog")
    }

    @Provides
    @Singleton
    override fun providePreferences(): Prefs {
        return PrefsImpl(App.instance)
    }

    @Provides
    @Singleton
    override fun provideTwitterClient(): Twitter {
        val conf: Configuration = ConfigurationBuilder()
            .setDebugEnabled(true)
            .setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY)
            .setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET)
            .setIncludeEmailEnabled(true)
            .build()
        val factory = TwitterFactory(conf)
        Logger.withTag("MyLog").log("AppModule Twitter new instance hashcode: ${factory.instance.hashCode()}")
        return factory.instance
    }

    @Provides
    @Singleton
    override fun provideTwitterNetworkClient(): TwitterNetworkClient {
        return TwitterApiClient()
    }

    @Provides
    override fun provideUtils(): Utils {
        return AndroidUtils(App.instance)
    }
}