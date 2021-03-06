package com.practice.socialclient.model.repositories.network.facebook

import com.practice.socialclient.model.repositories.network.facebook.schemas.NewsResponse
import com.practice.socialclient.model.repositories.network.facebook.schemas.UserDataResponse
import com.practice.socialclient.model.repositories.network.facebook.schemas.UserFriendsResponse
import com.practice.socialclient.model.repositories.network.facebook.schemas.UserPhotosResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FacebookApiService {

    @GET("v9.0/me/friends?fields=name,picture")
    fun getUserFriends(
        @Query("access_token") token: String?,
        @Query("limit") limit: String?
    ): Single<UserFriendsResponse>

    @GET("v9.0/me/friends?fields=name,picture")
    fun getNextFriendsPage(
        @Query("access_token") token: String?,
        @Query("after") cursorAfter: String?,
        @Query("limit") limit: String?
    ): Single<UserFriendsResponse>

    @GET("v9.0/me/")
    fun getUserData(
        @Query("access_token") token: String?,
        @Query("fields") fields: String
    ): Single<UserDataResponse>

    @GET("v9.0/me/photos/uploaded?fields=images")
    fun getUserPhotos(
        @Query("access_token") token: String?,
        @Query("limit") limit: String?
    ): Single<UserPhotosResponse>

    @GET("v9.0/me/photos/uploaded?fields=images")
    fun getNextUserPhotosPage(
        @Query("access_token") token: String?,
        @Query("after") cursorAfter: String?,
        @Query("limit") limit: String?
    ): Single<UserPhotosResponse>

    @GET("v9.0/me/feed?fields=attachments,type,created_time,message,reactions.summary(true),comments.summary(true)")
    fun getNewestNews(
        @Query("access_token") token: String?,
        @Query("limit") limit: String
    ): Single<NewsResponse>

    @GET("v9.0/me/feed?fields=attachments,type,created_time,message,reactions.summary(true),comments.summary(true)")
    fun getNewsWithUntilTime(
        @Query("access_token") token: String?,
        @Query("limit") limit: String,
        @Query("until") until: String
    ): Single<NewsResponse>
}
