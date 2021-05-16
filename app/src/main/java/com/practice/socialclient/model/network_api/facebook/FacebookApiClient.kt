package com.practice.socialclient.model.network_api.facebook

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.practice.socialclient.model.pojo.facebook_pojo.NewsResponse
import com.practice.socialclient.model.pojo.facebook_pojo.UserDataResponse
import com.practice.socialclient.model.pojo.facebook_pojo.UserFriendsResponse
import com.practice.socialclient.model.pojo.facebook_pojo.UserPhotosResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FacebookApiClient : FacebookNetworkClient {
    private val retrofit: Retrofit
    private val facebookApiService: FacebookApiService

    companion object {
        private const val BASE_URL = "https://graph.facebook.com/"
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
        facebookApiService = retrofit.create(FacebookApiService::class.java)
    }

    override fun getUserFriends(token: String?, limit: String?): Single<UserFriendsResponse> {
        return facebookApiService.getUserFriends(token, limit)
    }

    override fun getNextFriendsPage(token: String?, cursorAfter: String?, limit: String?): Single<UserFriendsResponse> {
        return facebookApiService.getNextFriendsPage(token, cursorAfter, limit)
    }

    override fun getUserData(token: String?): Single<UserDataResponse> {
        return facebookApiService.getUserData(token, "name, picture")
    }

    override fun getUserPhotos(token: String?, limit: String?): Single<UserPhotosResponse> {
        return facebookApiService.getUserPhotos(token, limit)
    }

    override fun getNextUserPhotosPage(token: String?, cursorAfter: String?, limit: String?): Single<UserPhotosResponse> {
        return facebookApiService.getNextUserPhotosPage(token, cursorAfter, limit)
    }

    override fun getUserNews(token: String?): Single<NewsResponse> {
        return  facebookApiService.getUserNews(token)
    }

    override fun getNews(token: String?, limit: Int): Single<NewsResponse> {
        return facebookApiService.getNewestNews(token, limit)
    }

    override fun getNewsWithUntilTime(token: String?, limit: Int, until: String): Single<NewsResponse> {
        return facebookApiService.getNewsWithUntilTime(token, limit, until)
    }

}