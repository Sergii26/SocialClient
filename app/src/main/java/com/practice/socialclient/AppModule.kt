package com.practice.socialclient

import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.repositories.auth.AuthRepository
import com.practice.socialclient.model.repositories.network.facebook.FacebookNetworkClient
import com.practice.socialclient.model.repositories.network.facebook.client.FacebookLoginManager
import com.practice.socialclient.model.repositories.network.twitter.TwitterNetworkClient
import com.practice.socialclient.model.repositories.network.twitter.client.TwitterClient
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

    fun provideFacebookLoginManager(): FacebookLoginManager

    fun provideFacebookFriendsRepository(): FriendsRepository

    fun provideTwitterFriendsRepository(): FriendsRepository

    fun provideFacebookPhotosRepository(): PhotosRepository

    fun provideTwitterPhotosRepository(): PhotosRepository

    fun provideFacebookNewsRepository(): NewsRepository

    fun provideTwitterNewsRepository(): NewsRepository

    fun provideFacebookUserInfoRepository(): UserInfoRepository

    fun provideTwitterUserInfoRepository(): UserInfoRepository

    fun provideFacebookAuthRepository(): AuthRepository

    fun provideTwitterAuthRepository(): AuthRepository

}