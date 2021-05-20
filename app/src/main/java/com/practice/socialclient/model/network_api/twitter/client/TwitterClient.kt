package com.practice.socialclient.model.network_api.twitter.client

import twitter4j.HttpParameter
import twitter4j.RequestMethod
import twitter4j.auth.AccessToken
import twitter4j.auth.RequestToken

interface TwitterClient {
    fun getAuthHeader(
        parameters: Array<HttpParameter>,
        requestMethod: RequestMethod,
        requestUrl: String
    ): String

    fun getRequestToken(): RequestToken?
    fun getAccessToken(verifier: String): AccessToken?
    fun setAccToken(token: String, secret: String)
    fun cleanAccToken()
}