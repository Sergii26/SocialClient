package com.practice.socialclient.model.network_api.twitter

import com.practice.socialclient.model.network_api.twitter.schemas.FriendsResponse
import com.practice.socialclient.model.network_api.twitter.schemas.TweetsResponse
import com.practice.socialclient.model.network_api.twitter.schemas.User
import com.practice.socialclient.model.network_api.twitter.schemas.UserInformation
import io.reactivex.Single
import twitter4j.Twitter

interface TwitterNetworkClient {
    fun getTweets(count: String): Single<Array<TweetsResponse>>
    fun getUserTweets(count: String): Single<Array<TweetsResponse>>
    fun getTweetsOlderThan(lastTweetId: Long, count: String): Single<Array<TweetsResponse>>
    fun getUserTweetsOlderThan(lastTweetId: Long, count: String): Single<Array<TweetsResponse>>
    fun getFriends(count: String): Single<FriendsResponse>
    fun getNextFriendsPage(count: String, cursor: String): Single<FriendsResponse>
    fun getFriendsCount(): Single<UserInformation>
    fun getUserData(): Single<User>
    fun isLoggedIn(): Single<Any>
}
