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
import dagger.Provides
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.conf.Configuration
import twitter4j.conf.ConfigurationBuilder
import javax.inject.Singleton

interface NewAppModule {

    fun provideNetworkClient(): FacebookNetworkClient


    fun provideILog(): ILog

    fun providePreferences(): Prefs


    fun provideTwitterClient(): Twitter


    fun provideTwitterNetworkClient(): TwitterNetworkClient


    fun provideUtils(): Utils
}