package com.practice.socialclient.model.network_api.facebook

import com.practice.socialclient.model.schemas.*
import io.reactivex.Single

interface FacebookNetworkClient {
    fun getUserFriendsCount(token: String?, limit: String?): Single<FriendsCountInfo>
    fun getUserFriends(token: String?, limit: String?): Single<List<FriendInfo>>

    fun getNextFriendsPage(
        token: String?, cursorAfter: String?, limit: String?
    ): Single<List<FriendInfo>>
    fun getUserData(token: String?): Single<UserInfo>
    fun getUserPhotos(token: String?, limit: String?): Single<List<PhotoInfo>>
    fun getNextUserPhotosPage(
        token: String?, cursorAfter: String?, limit: String?
    ): Single<List<PhotoInfo>>
    fun getNews(token: String?, limit: String): Single<List<NewsInfo>>
    fun getNewsWithUntilTime(token: String?, limit: String, until: String): Single<List<NewsInfo>>
}
