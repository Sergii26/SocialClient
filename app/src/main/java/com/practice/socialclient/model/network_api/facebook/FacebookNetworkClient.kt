package com.practice.socialclient.model.network_api.facebook

import com.practice.socialclient.model.network_api.facebook.schemas.NewsResponse
import com.practice.socialclient.model.network_api.facebook.schemas.UserDataResponse
import com.practice.socialclient.model.network_api.facebook.schemas.UserFriendsResponse
import com.practice.socialclient.model.network_api.facebook.schemas.UserPhotosResponse
import io.reactivex.Single

interface FacebookNetworkClient {
    fun getUserFriends(token: String?, limit: String?): Single<UserFriendsResponse>
    fun getNextFriendsPage(
        token: String?, cursorAfter: String?, limit: String?
    ): Single<UserFriendsResponse>

    fun getUserData(token: String?): Single<UserDataResponse>
    fun getUserPhotos(token: String?, limit: String?): Single<UserPhotosResponse>
    fun getNextUserPhotosPage(
        token: String?, cursorAfter: String?, limit: String?
    ): Single<UserPhotosResponse>

    fun getUserNews(token: String?): Single<NewsResponse>
    fun getNews(token: String?, limit: String): Single<NewsResponse>
    fun getNewsWithUntilTime(token: String?, limit: String, until: String): Single<NewsResponse>
}
