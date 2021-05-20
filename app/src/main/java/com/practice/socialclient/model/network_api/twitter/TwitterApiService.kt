package com.practice.socialclient.model.network_api.twitter

import com.practice.socialclient.model.network_api.twitter.schemas.FriendsResponse
import com.practice.socialclient.model.network_api.twitter.schemas.TweetsResponse
import com.practice.socialclient.model.network_api.twitter.schemas.User
import com.practice.socialclient.model.network_api.twitter.schemas.UserInformation
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface TwitterApiService {

    @GET("1.1/statuses/home_timeline.json")
    fun getTweets(
        @Header("Authorization") authorizationHeader: String, @Query("count") count: String,
        @Query("tweet_mode") tweetMode: String
    ): Single<Array<TweetsResponse>>

    @GET("1.1/statuses/user_timeline.json")
    fun getUserTweets(
        @Header("Authorization") authorizationHeader: String, @Query("count") count: String,
        @Query("tweet_mode") tweetMode: String
    ): Single<Array<TweetsResponse>>

    @GET("1.1/statuses/user_timeline.json")
    fun getUserTweetsOlderThan(
        @Header("Authorization") authorizationHeader: String, @Query("max_id") lastTweetId: Long,
        @Query("count") count: String, @Query("tweet_mode") tweetMode: String
    ): Single<Array<TweetsResponse>>

    @GET("1.1/statuses/home_timeline.json")
    fun getTweetsOlderThan(
        @Header("Authorization") authorizationHeader: String, @Query("max_id") lastTweetId: Long,
        @Query("count") count: String, @Query("tweet_mode") tweetMode: String
    ): Single<Array<TweetsResponse>>

    @GET("1.1/friends/list.json")
    fun getFriends(
        @Header("Authorization") authorizationHeader: String,
        @Query("count") count: String
    ): Single<FriendsResponse>

    @GET("1.1/friends/list.json")
    fun getNextFriendsPage(
        @Header("Authorization") authorizationHeader: String,
        @Query("count") count: String, @Query("cursor") cursor: String
    ): Single<FriendsResponse>

    @GET("1.1/account/verify_credentials.json")
    fun getFriendsCount(@Header("Authorization") authorizationHeader: String): Single<UserInformation>

    @GET("1.1/account/verify_credentials.json")
    fun getUserData(@Header("Authorization") authorizationHeader: String): Single<User>

    @GET("1.1/account/verify_credentials.json")
    fun isLoggedIn(@Header("Authorization") authorizationHeader: String): Single<Any>
}
