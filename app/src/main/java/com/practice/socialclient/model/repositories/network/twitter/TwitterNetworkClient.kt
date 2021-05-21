package com.practice.socialclient.model.repositories.network.twitter

import com.practice.socialclient.model.dto.*
import io.reactivex.Single

interface TwitterNetworkClient {
    fun getTweets(count: String): Single<List<NewsInfo>>
    fun getPhotos(count: String): Single<List<PhotoInfo>>
    fun getTweetsOlderThan(lastTweetId: Long, count: String): Single<List<NewsInfo>>
    fun getPhotosOlderThan(lastTweetId: Long, count: String): Single<List<PhotoInfo>>
    fun getFriends(count: String): Single<List<FriendInfo>>
    fun getNextFriendsPage(count: String, cursor: String): Single<List<FriendInfo>>
    fun getFriendsCount(): Single<FriendsCountInfo>
    fun getUserData(): Single<UserInfo>
    fun isLoggedIn(): Single<Any>
}
