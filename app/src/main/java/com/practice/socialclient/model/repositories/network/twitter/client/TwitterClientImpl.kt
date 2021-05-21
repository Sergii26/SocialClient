package com.practice.socialclient.model.repositories.network.twitter.client

import io.reactivex.Single
import twitter4j.HttpParameter
import twitter4j.HttpRequest
import twitter4j.RequestMethod
import twitter4j.Twitter
import twitter4j.auth.AccessToken

class TwitterClientImpl(private val twitterClient: Twitter) : TwitterClient {

    override fun setAccToken(token: String, secret: String) {
        twitterClient.oAuthAccessToken =
            AccessToken(token, secret)
    }

    override fun cleanAccToken() {
        twitterClient.oAuthAccessToken = null
    }

    override fun getAuthHeader(
        parameters: Array<HttpParam>,
        requestMethod: String,
        requestUrl: String
    ): String {
        return twitterClient.authorization.getAuthorizationHeader(
            HttpRequest(
                defineRequestMethod(requestMethod),
                requestUrl,
                convertParams(parameters),
                twitterClient.authorization,
                null
            )
        )
    }

    override fun getAuthUrl(): Single<String> {
        return Single.fromCallable { twitterClient.oAuthRequestToken }
            .map { response ->
                response.authorizationURL
            }
    }


    override fun getAccessToken(verifier: String): Single<AccToken> {
        return Single.fromCallable { twitterClient.getOAuthAccessToken(verifier) }
            .map { response ->
                AccToken(response.token, response.tokenSecret)
            }
    }

    private fun defineRequestMethod(method: String): RequestMethod{
        return when(method){
            "GET" -> RequestMethod.GET
            "PUT" -> RequestMethod.PUT
            "DELETE" -> RequestMethod.DELETE
            "HEAD" -> RequestMethod.HEAD
            else -> throw Exception("unsupported request method")
        }
    }

    private fun convertParams(params: Array<HttpParam>): Array<HttpParameter> {
        var convertedParameters = ArrayList<HttpParameter>()
        params.forEach { it->
            convertedParameters.add(HttpParameter(it.param, it.value))
        }
        return convertedParameters.toTypedArray()
    }
}