package com.practice.socialclient.model.network_api.twitter

import com.practice.socialclient.model.pojo.twitter_pojo.*
import io.reactivex.Single
import twitter4j.Twitter

interface TwitterNetworkClient {
    fun getTweets(count: String, twitterClient: Twitter): Single<Array<TweetsResponse>>
    fun getUserTweets(count: String, twitterClient: Twitter): Single<Array<TweetsResponse>>
    fun getTweetsOlderThan(lastTweetId: Long, count: String, twitterClient: Twitter): Single<Array<TweetsResponse>>
    fun getUserTweetsOlderThan(lastTweetId: Long, count: String, twitterClient: Twitter): Single<Array<TweetsResponse>>
    fun getFriends(count: String, twitterClient: Twitter): Single<FriendsResponse>
    fun getNextFriendsPage(count: String, cursor: String, twitterClient: Twitter): Single<FriendsResponse>
    fun getFriendsCount(twitterClient: Twitter): Single<UserInformation>
    fun getUserData(twitterClient: Twitter): Single<User>
    fun isLoggedIn(twitterClient: Twitter): Single<Any>
}