package com.practice.socialclient.model.repositories.network.twitter

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.practice.socialclient.model.repositories.network.twitter.header_factory.HttpParam
import com.practice.socialclient.model.repositories.network.twitter.header_factory.TwitterHeaderFactory
import com.practice.socialclient.model.repositories.network.twitter.schemas.FriendsResponse
import com.practice.socialclient.model.repositories.network.twitter.schemas.TweetsResponse
import com.practice.socialclient.model.dto.*
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TwitterApiClient(private val twitterHeaderFactory: TwitterHeaderFactory) : TwitterNetworkClient {
    private val apiService: TwitterApiService

    companion object {
        private const val BASE_URL = "https://api.twitter.com/"
        private const val REQUEST_METHOD_GET = "GET"
        private const val twitterSource = "Twitter friends: "
    }

    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        apiService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TwitterApiService::class.java)
    }

    override fun getTweets(count: String): Single<List<NewsInfo>> {
        val parameters =
            arrayOf(HttpParam("count", count), HttpParam("tweet_mode", "extended"))
        val requestUrl = "https://api.twitter.com/1.1/statuses/home_timeline.json"
        val authHeader = twitterHeaderFactory.createAuthHeader(parameters, REQUEST_METHOD_GET, requestUrl)
        return apiService.getTweets(authHeader, count, "extended")
            .map { response -> convertTwNews(response) }
    }

    override fun getTweetsOlderThan(
        lastTweetId: Long,
        count: String
    ): Single<List<NewsInfo>> {

        val parameters = arrayOf(
            HttpParam("count", count), HttpParam("tweet_mode", "extended"),
            HttpParam("max_id", lastTweetId.toString())
        )
        val requestUrl = "https://api.twitter.com/1.1/statuses/home_timeline.json"
        val authHeader = twitterHeaderFactory.createAuthHeader(parameters, REQUEST_METHOD_GET, requestUrl)
        return apiService.getTweetsOlderThan(authHeader, lastTweetId, count, "extended")
            .map { response -> convertTwNews(response) }
    }

    override fun getPhotos(
        count: String
    ): Single<List<PhotoInfo>> {
        val parameters =
            arrayOf(HttpParam("count", count), HttpParam("tweet_mode", "extended"))
        val requestUrl = "https://api.twitter.com/1.1/statuses/user_timeline.json"
        val authHeader = twitterHeaderFactory.createAuthHeader(parameters, REQUEST_METHOD_GET, requestUrl)
        return apiService.getUserTweets(authHeader, count, "extended")
            .map { response -> convertTwPhotosResponse(response) }
    }

    override fun getPhotosOlderThan(
        lastTweetId: Long,
        count: String
    ): Single<List<PhotoInfo>> {

        val parameters = arrayOf(
            HttpParam("count", count), HttpParam("tweet_mode", "extended"),
            HttpParam("max_id", lastTweetId.toString())
        )
        val requestUrl = "https://api.twitter.com/1.1/statuses/user_timeline.json"
        val authHeader = twitterHeaderFactory.createAuthHeader(parameters, REQUEST_METHOD_GET, requestUrl)
        return apiService.getUserTweetsOlderThan(authHeader, lastTweetId, count, "extended")
            .map { response -> convertTwPhotosResponse(response) }
    }



    override fun getFriends(count: String): Single<List<FriendInfo>> {
        val parameters = arrayOf(HttpParam("count", count))
        val requestUrl = "https://api.twitter.com/1.1/friends/list.json"
        val authHeader = twitterHeaderFactory.createAuthHeader(parameters, REQUEST_METHOD_GET, requestUrl)
        return apiService.getFriends(authHeader, count)
            .map { response -> convertTwFriendsResponse(response) }
    }

    override fun getNextFriendsPage(
        count: String,
        cursor: String
    ): Single<List<FriendInfo>> {
        val parameters =
            arrayOf(HttpParam("count", count), HttpParam("cursor", cursor))
        val requestUrl = "https://api.twitter.com/1.1/friends/list.json"
        val authHeader = twitterHeaderFactory.createAuthHeader(parameters, REQUEST_METHOD_GET, requestUrl)
        return apiService.getNextFriendsPage(authHeader, count, cursor)
            .map { response -> convertTwFriendsResponse(response) }
    }

    override fun getFriendsCount(): Single<FriendsCountInfo> {
        val requestUrl = "https://api.twitter.com/1.1/account/verify_credentials.json"
        val authHeader = twitterHeaderFactory.createAuthHeader(emptyArray(), REQUEST_METHOD_GET, requestUrl)
        return apiService.getFriendsCount(authHeader)
            .map { response ->
                response.friendsCount.toString()
                FriendsCountInfo(twitterSource, response.friendsCount.toString())
            }
    }

    override fun getUserData(): Single<UserInfo> {
        val requestUrl = "https://api.twitter.com/1.1/account/verify_credentials.json"
        val authHeader = twitterHeaderFactory.createAuthHeader(emptyArray(), REQUEST_METHOD_GET, requestUrl)
        return apiService.getUserData(authHeader)
            .map { response ->
                UserInfo(response.name.toString(), response.profileImageUrlHttps.toString())
            }
    }

    override fun isLoggedIn(): Single<Any> {
        val requestUrl = "https://api.twitter.com/1.1/account/verify_credentials.json"
        val authHeader = twitterHeaderFactory.createAuthHeader(emptyArray(), REQUEST_METHOD_GET, requestUrl)
        return apiService.isLoggedIn(authHeader)
    }

    private fun convertTwFriendsResponse(response: FriendsResponse): MutableList<FriendInfo> {
        val convertedResponse: MutableList<FriendInfo> = ArrayList()
        response.friends.forEach {
            convertedResponse.add(
                FriendInfo(
                    it.name.toString(),
                    it.profileImageUrlHttps.toString(),
                    FriendInfo.SOURCE_TWITTER,
                    response.nextCursorStr
                )
            )
        }
        return convertedResponse
    }

    private fun convertTwPhotosResponse(response: Array<TweetsResponse>): MutableList<PhotoInfo> {
        val convertedResponse: MutableList<PhotoInfo> = ArrayList()
        response.forEach {
            if (it.entities?.mediaArray?.get(0)?.mediaUrl.toString() != "null") {
                convertedResponse.add(
                    PhotoInfo(
                        it.entities?.mediaArray?.get(0)?.mediaUrl.toString(),
                        it.id.toString()
                    )
                )
            }
        }
        return convertedResponse
    }

    private fun convertTwNews(twNewResponses: Array<TweetsResponse>): MutableList<NewsInfo> {
        val convertedNews: MutableList<NewsInfo> = ArrayList()
//        changeTwDatePattern(twNewResponses.get(0).createdAt.toString())
        twNewResponses.forEach {
            convertedNews.add(
                NewsInfo(
                    it.id.toString(),
                    it.fullText.toString(),
                    it.entities?.mediaArray?.get(0)?.mediaUrl.toString(),
                    it.user?.profileImageUrlHttps.toString(),
                    it.user?.name.toString(),
                    it.likeCount.toString(),
                    it.retweetCount.toString(),
                    convertTwUTCTimeToUnix(it.createdAt.toString()),
                    NewsInfo.SOURCE_TWITTER,
                    changeTwDatePattern(it.createdAt.toString()),
                    it.id.toString(),
                )
            )
        }
        return convertedNews
    }

    private fun changeTwDatePattern(UTCTime: String): String {
        val unixTime = convertTwUTCTimeToUnix(UTCTime).toLong()
        val date1 = Date(unixTime * 1000L)
        val sdf = SimpleDateFormat("MMMM dd, hh:mm aaa")
        return sdf.format(date1)
    }

    private fun convertTwUTCTimeToUnix(UTCTime: String): Long {
        val sdf = SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.ENGLISH)
        val date = sdf.parse(UTCTime)
        return date.time / 1000
    }

}