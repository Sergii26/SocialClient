package com.practice.socialclient.model.repositories.network.twitter.client
import io.reactivex.Single

interface TwitterClient {
    fun getAuthHeader(
        parameters: Array<HttpParam>,
        requestMethod: String,
        requestUrl: String
    ): String

    fun getAuthUrl(): Single<String>
    fun getAccessToken(verifier: String): Single<AccToken>
    fun setAccToken(token: String, secret: String)
    fun cleanAccToken()
}