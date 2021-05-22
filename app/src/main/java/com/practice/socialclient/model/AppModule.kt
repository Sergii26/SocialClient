package com.practice.socialclient.model

import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.model.repositories.auth.facebook.FacebookAuthRepository
import com.practice.socialclient.model.repositories.auth.twitter.TwitterAuthRepository
import com.practice.socialclient.model.repositories.friends.FriendsRepository
import com.practice.socialclient.model.repositories.network.facebook.FacebookNetworkClient
import com.practice.socialclient.model.repositories.network.twitter.TwitterNetworkClient
import com.practice.socialclient.model.repositories.network.twitter.header_factory.TwitterHeaderFactory
import com.practice.socialclient.model.repositories.news.NewsRepository
import com.practice.socialclient.model.repositories.photos.PhotosRepository
import com.practice.socialclient.model.repositories.user.UserInfoRepository
import com.practice.socialclient.model.utils.Utils

interface AppModule {

    fun provideFacebookNetworkClient(): FacebookNetworkClient

    fun provideILog(): Log

    fun providePreferences(): Prefs

    fun provideTwitterNetworkClient(): TwitterNetworkClient

    fun provideUtils(): Utils

    fun provideTwitterHeaderFactory(): TwitterHeaderFactory

    fun provideFacebookFriendsRepository(): FriendsRepository

    fun provideTwitterFriendsRepository(): FriendsRepository

    fun provideFacebookPhotosRepository(): PhotosRepository

    fun provideTwitterPhotosRepository(): PhotosRepository

    fun provideFacebookNewsRepository(): NewsRepository

    fun provideTwitterNewsRepository(): NewsRepository

    fun provideFacebookUserInfoRepository(): UserInfoRepository

    fun provideTwitterUserInfoRepository(): UserInfoRepository

    fun provideFacebookAuthRepository(): FacebookAuthRepository

    fun provideTwitterAuthRepository(): TwitterAuthRepository
}