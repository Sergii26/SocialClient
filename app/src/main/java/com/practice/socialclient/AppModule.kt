package com.practice.socialclient

import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.utils_login.LogOutUtil
import com.practice.socialclient.model.utils_login.LoginStateUtil
import com.practice.socialclient.model.network_api.facebook.FacebookNetworkClient
import com.practice.socialclient.model.network_api.facebook.LoginManager
import com.practice.socialclient.model.network_api.twitter.TwitterNetworkClient
import com.practice.socialclient.model.network_api.twitter.client.TwitterClient
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.model.repositories.friends.FriendsRepository
import com.practice.socialclient.model.repositories.news.NewsRepository
import com.practice.socialclient.model.repositories.photos.PhotosRepository
import com.practice.socialclient.model.repositories.user.UserInfoRepository
import com.practice.socialclient.model.utils_android.Utils

interface AppModule {

    fun provideFacebookNetworkClient(): FacebookNetworkClient

    fun provideILog(): Log

    fun providePreferences(): Prefs

    fun provideTwitterClient(): TwitterClient

    fun provideTwitterNetworkClient(): TwitterNetworkClient

    fun provideUtils(): Utils

    fun provideFacebookFriendsRepository(): FriendsRepository

    fun provideTwitterFriendsRepository(): FriendsRepository

    fun provideFacebookPhotosRepository(): PhotosRepository

    fun provideTwitterPhotosRepository(): PhotosRepository

    fun provideFacebookNewsRepository(): NewsRepository

    fun provideTwitterNewsRepository(): NewsRepository

    fun provideFacebookUserInfoRepository(): UserInfoRepository

    fun provideTwitterUserInfoRepository(): UserInfoRepository

    fun provideFacebookStateLoginUtil(): LoginStateUtil

    fun provideTwitterStateLoginUtil(): LoginStateUtil

    fun provideFacebookLoginManager(): LoginManager

    fun provideLogOutUtil(): LogOutUtil

}