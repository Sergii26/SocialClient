package com.practice.socialclient.model.network_api.facebook

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.practice.socialclient.model.network_api.facebook.schemas.NewsResponse
import com.practice.socialclient.model.network_api.facebook.schemas.UserDataResponse
import com.practice.socialclient.model.network_api.facebook.schemas.UserFriendsResponse
import com.practice.socialclient.model.network_api.facebook.schemas.UserPhotosResponse
import com.practice.socialclient.model.schemas.*
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FacebookApiClient : FacebookNetworkClient {
    private val facebookApiService: FacebookApiService

    companion object {
        private const val BASE_URL = "https://graph.facebook.com/"
        private const val zero = "0"
        private const val facebookSource = "Facebook friends: "
    }

    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        facebookApiService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(FacebookApiService::class.java)
    }

    override fun getUserFriends(token: String?, limit: String?): Single<List<FriendInfo>> {
        return facebookApiService.getUserFriends(token, limit)
            .map { response ->
            convertFbFriendsResponse(response)
        }
    }

    override fun getUserFriendsCount(token: String?, limit: String?): Single<FriendsCountInfo> {
        return facebookApiService.getUserFriends(token, limit)
            .map { response -> FriendsCountInfo(facebookSource, response.summary?.totalCount?.toString() ?: zero) }
    }

    override fun getNextFriendsPage(
        token: String?,
        cursorAfter: String?,
        limit: String?
    ): Single<List<FriendInfo>> {
        return facebookApiService.getNextFriendsPage(token, cursorAfter, limit)
            .map { response -> convertFbFriendsResponse(response) }
    }

    override fun getUserData(token: String?): Single<UserInfo> {
        return facebookApiService.getUserData(token, "name, picture")
            .map { response ->
                UserInfo(response.name.toString(),response.pictureData?.picture?.url.toString())
            }
    }

    override fun getUserPhotos(token: String?, limit: String?): Single<List<PhotoInfo>> {
        return facebookApiService.getUserPhotos(token, limit)
            .map { response -> convertFbPhotosResponse(response)}

    }

    override fun getNextUserPhotosPage(
        token: String?,
        cursorAfter: String?,
        limit: String?
    ): Single<List<PhotoInfo>> {
        return facebookApiService.getNextUserPhotosPage(token, cursorAfter, limit)
            .map { response -> convertFbPhotosResponse(response)}
    }

    override fun getNews(token: String?, limit: String): Single<List<NewsInfo>> {
        return facebookApiService.getNewestNews(token, limit)
            .zipWith(facebookApiService.getUserData(token, "name, picture"),
                BiFunction { newsResponse, userData -> convertFbNews(newsResponse, userData)})

    }

    override fun getNewsWithUntilTime(
        token: String?,
        limit: String,
        until: String
    ): Single<List<NewsInfo>> {
        return facebookApiService.getNewsWithUntilTime(token, limit, until)
            .zipWith(facebookApiService.getUserData(token, "name, picture"),
                BiFunction { newsResponse, userData -> convertFbNews(newsResponse, userData)})

    }


    private fun convertFbPhotosResponse(response: UserPhotosResponse): MutableList<PhotoInfo> {
        val dataResponse = response.data
        val convertedResponse: MutableList<PhotoInfo> = ArrayList()
        dataResponse?.forEach {
            convertedResponse.add(
                PhotoInfo(
                    it.images?.get(0)?.src.toString(),
                    response.paging?.cursors?.after.toString()
                )
            )
        }
        return convertedResponse
    }


    private fun convertFbFriendsResponse(response: UserFriendsResponse): MutableList<FriendInfo> {
        val dataResponse = response.data
        val convertedResponse: MutableList<FriendInfo> = ArrayList()
        dataResponse?.forEach {
            convertedResponse.add(
                FriendInfo(
                    it.name.toString(),
                    it.picture?.data1te?.url.toString(),
                    FriendInfo.SOURCE_FACEBOOK,
                    response.paging?.cursors?.after.toString()
                )
            )
        }
        return convertedResponse
    }

    private fun convertFbNews(fbNews: NewsResponse, userData: UserDataResponse): MutableList<NewsInfo> {
        val convertedNews: MutableList<NewsInfo> = ArrayList()
        val responseList = fbNews.newsData as ArrayList
        responseList.forEach {
            convertedNews.add(
                NewsInfo(
                    it?.id.toString(),
                    it?.message ?: "",
                    it?.attachments?.attachmentsData?.get(0)?.media?.image?.src.toString(),
                    userData.pictureData?.picture?.url.toString(),
                    userData.name.toString(),
                    it?.comments?.summary?.totalCount?.toString() ?: "0",
                    it?.reactions?.summary?.totalCount?.toString() ?: "0",
                    convertFbUTCTimeToUnix(it?.createdTime.toString()),
                    NewsInfo.SOURCE_FACEBOOK,
                    changeFbDatePattern(it?.createdTime.toString()),
                    fbNews.paging?.cursors?.after.toString()
                )
            )
        }
        return convertedNews
    }

    private fun convertFbUTCTimeToUnix(UTCTime: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH)
        val date = sdf.parse(UTCTime)
        return date.time / 1000
    }

    private fun changeFbDatePattern(UTCTime: String): String {
        val unixTime = convertFbUTCTimeToUnix(UTCTime).toLong()
        val date1 = Date(unixTime * 1000L)
        val sdf = SimpleDateFormat("MMMM dd, hh:mm aaa")
        val formattedDate = sdf.format(date1)
        return formattedDate
    }

}