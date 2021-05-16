package com.practice.socialclient.model.network_api.facebook

import com.practice.socialclient.model.pojo.facebook_pojo.NewsResponse
import com.practice.socialclient.model.pojo.facebook_pojo.UserDataResponse
import com.practice.socialclient.model.pojo.facebook_pojo.UserFriendsResponse
import com.practice.socialclient.model.pojo.facebook_pojo.UserPhotosResponse
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
    fun getNews(token: String?, limit: Int): Single<NewsResponse>
    fun getNewsWithUntilTime(token: String?, limit: Int, until: String): Single<NewsResponse>
}
