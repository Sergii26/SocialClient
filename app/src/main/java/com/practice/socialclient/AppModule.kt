//package com.practice.socialclient
//
//import com.practice.socialclient.model.logger.ILog
//import com.practice.socialclient.model.logger.Logger
//import com.practice.socialclient.model.network_api.facebook.FacebookApiClient
//import com.practice.socialclient.model.network_api.facebook.FacebookNetworkClient
//import com.practice.socialclient.model.network_api.twitter.TwitterApiClient
//import com.practice.socialclient.model.network_api.twitter.TwitterNetworkClient
//import com.practice.socialclient.model.prefs.Prefs
//import com.practice.socialclient.model.prefs.PrefsImpl
//import com.practice.socialclient.model.twitter.TwitterConstants
//import com.practice.socialclient.model.utils.AndroidUtils
//import com.practice.socialclient.model.utils.Utils
//import dagger.Module
//import dagger.Provides
//import twitter4j.Twitter
//import twitter4j.TwitterFactory
//import twitter4j.conf.Configuration
//import twitter4j.conf.ConfigurationBuilder
//import javax.inject.Singleton
//
//
//@Module
//class AppModule {
//
//    private val appComponent: AppComponent? = com.practice.socialclient.App.instance?.appComponent
//    private val logger = Logger.withTag("MyLog")
//
//    @Provides
//    @Singleton
//    fun provideNetworkClient(): FacebookNetworkClient {
//        val nc = FacebookApiClient()
//        return nc
//    }
//
//    @Provides
//    fun provideILog(): ILog {
//        return Logger.withTag("MyLog")
//    }
//
//    @Provides
//    @Singleton
//    fun providePreferences(): Prefs {
//        return PrefsImpl(App.instance)
//    }
//
//    @Provides
//    @Singleton
//    fun provideTwitterClient(): Twitter {
//        val conf: Configuration = ConfigurationBuilder()
//                .setDebugEnabled(true)
//                .setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY)
//                .setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET)
//                .setIncludeEmailEnabled(true)
//                .build()
//        val factory = TwitterFactory(conf)
//        return factory.instance
//    }
//
//    @Provides
//    fun provideTwitterNetworkClient(): TwitterNetworkClient {
//        return TwitterApiClient()
//    }
//
//    @Provides
//    fun provideUtils(): Utils {
//        return AndroidUtils(App.instance)
//    }
//}