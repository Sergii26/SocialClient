package com.practice.socialclient.model.network_api.twitter.client

import twitter4j.HttpParameter
import twitter4j.HttpRequest
import twitter4j.RequestMethod
import twitter4j.Twitter
import twitter4j.auth.AccessToken
import twitter4j.auth.RequestToken

class TwitterClientImpl(private val twitterClient: Twitter):TwitterClient {

    override fun setAccToken(token: String, secret: String) {
        twitterClient.oAuthAccessToken =
            AccessToken(token, secret)
    }

    override fun cleanAccToken(){
        twitterClient.oAuthAccessToken = null
    }

    override fun getAuthHeader(
        parameters: Array<HttpParameter>,
        requestMethod: RequestMethod,
        requestUrl: String
    ): String {
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

    override fun getRequestToken(): RequestToken? {
        return twitterClient.oAuthRequestToken
    }

    override fun getAccessToken(verifier: String): AccessToken? {
        return twitterClient.getOAuthAccessToken(verifier)
    }
}