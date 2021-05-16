package com.practice.socialclient.model.network_api.twitter

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.practice.socialclient.model.pojo.twitter_pojo.*
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import twitter4j.HttpParameter
import twitter4j.HttpRequest
import twitter4j.RequestMethod
import twitter4j.Twitter


class TwitterApiClient : TwitterNetworkClient {
    private val retrofit: Retrofit
    private val apiService: TwitterApiService

    companion object {
        private const val BASE_URL = "https://api.twitter.com/"
    }

    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        apiService = retrofit.create(TwitterApiService::class.java)
    }

    override fun getTweets(count: String, twitterClient: Twitter): Single<Array<TweetsResponse>> {
        val parameters = arrayOf(HttpParameter("count", count), HttpParameter("tweet_mode", "extended"))
        val requestUrl = "https://api.twitter.com/1.1/statuses/home_timeline.json"
        val authHeader = getTwAuthHeader(twitterClient, parameters, RequestMethod.GET, requestUrl)
        return apiService.getTweets(authHeader, count, "extended")
    }

    override fun getUserTweets(count: String, twitterClient: Twitter): Single<Array<TweetsResponse>> {
        val parameters = arrayOf(HttpParameter("count", count), HttpParameter("tweet_mode", "extended"))
        val requestUrl = "https://api.twitter.com/1.1/statuses/user_timeline.json"
        val authHeader = getTwAuthHeader(twitterClient, parameters, RequestMethod.GET, requestUrl)
        return apiService.getUserTweets(authHeader, count, "extended")
    }

    override fun getUserTweetsOlderThan(
        lastTweetId: Long,
        count: String,
        twitterClient: Twitter
    ): Single<Array<TweetsResponse>> {

        val parameters = arrayOf(HttpParameter("count", count), HttpParameter("tweet_mode", "extended"),
            HttpParameter("max_id", lastTweetId))
        val requestUrl = "https://api.twitter.com/1.1/statuses/user_timeline.json"
        val authHeader = getTwAuthHeader(twitterClient, parameters, RequestMethod.GET, requestUrl)
        return apiService.getUserTweetsOlderThan(authHeader, lastTweetId, count, "extended")
    }

    override fun getTweetsOlderThan(
        lastTweetId: Long,
        count: String,
        twitterClient: Twitter
    ): Single<Array<TweetsResponse>> {

        val parameters = arrayOf(HttpParameter("count", count), HttpParameter("tweet_mode", "extended"),
            HttpParameter("max_id", lastTweetId))
        val requestUrl = "https://api.twitter.com/1.1/statuses/home_timeline.json"
        val authHeader = getTwAuthHeader(twitterClient, parameters, RequestMethod.GET, requestUrl)
        return apiService.getTweetsOlderThan(authHeader, lastTweetId, count, "extended")
    }

    override fun getFriends(count: String, twitterClient: Twitter): Single<FriendsResponse> {
        val parameters = arrayOf(HttpParameter("count", count))
        val requestUrl = "https://api.twitter.com/1.1/friends/list.json"
        val authHeader = getTwAuthHeader(twitterClient, parameters, RequestMethod.GET, requestUrl)
        return apiService.getFriends(authHeader, count)
    }

    override fun getNextFriendsPage(
        count: String,
        cursor: String,
        twitterClient: Twitter
    ): Single<FriendsResponse> {
        val parameters =
            arrayOf(HttpParameter("count", count), HttpParameter("cursor", cursor))
        val requestUrl = "https://api.twitter.com/1.1/friends/list.json"
        val authHeader = getTwAuthHeader(twitterClient, parameters, RequestMethod.GET, requestUrl)
        return apiService.getNextFriendsPage(authHeader, count, cursor)
    }

    override fun getFriendsCount(twitterClient: Twitter): Single<UserInformation> {
        val requestUrl = "https://api.twitter.com/1.1/account/verify_credentials.json"
        val authHeader = getTwAuthHeader(twitterClient, emptyArray(), RequestMethod.GET, requestUrl)
        return apiService.getFriendsCount(authHeader)
    }

    override fun getUserData(twitterClient: Twitter): Single<User> {
        val requestUrl = "https://api.twitter.com/1.1/account/verify_credentials.json"
        val authHeader = getTwAuthHeader(twitterClient, emptyArray(), RequestMethod.GET, requestUrl)
        return apiService.getUserData(authHeader)
    }

    override fun isLoggedIn(twitterClient: Twitter): Single<Any> {
        val requestUrl = "https://api.twitter.com/1.1/account/verify_credentials.json"
        val authHeader = getTwAuthHeader(twitterClient, emptyArray(), RequestMethod.GET, requestUrl)
        return apiService.isLoggedIn(authHeader)
    }

    private fun getTwAuthHeader(twitterClient: Twitter, parameters: Array<HttpParameter>, requestMethod: RequestMethod, requestUrl: String): String {
        return twitterClient.authorization.getAuthorizationHeader(
            HttpRequest(
                requestMethod,
                requestUrl,
                parameters,
                twitterClient.authorization,
                null
            )
        )
    }

}