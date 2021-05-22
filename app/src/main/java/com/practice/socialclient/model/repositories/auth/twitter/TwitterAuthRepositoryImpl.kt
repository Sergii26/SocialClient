package com.practice.socialclient.model.repositories.auth.twitter

import com.practice.socialclient.model.repositories.network.twitter.header_factory.HttpParam
import io.reactivex.Single
import twitter4j.HttpParameter
import twitter4j.RequestMethod
import twitter4j.Twitter
import twitter4j.auth.AccessToken

class TwitterAuthRepositoryImpl(
    private val twitterClient: Twitter
) : TwitterAuthRepository{

    override fun setTwitterAccToken(token: String, secret: String) {
        twitterClient.oAuthAccessToken =
            AccessToken(token, secret)
    }

    override fun cleanTwitterAccToken() {
        twitterClient.oAuthAccessToken = null
    }

    override fun getTwitterAuthUrl(): Single<String> {
        return Single.fromCallable { twitterClient.oAuthRequestToken }
            .map { response ->
                response.authorizationURL
            }
    }


    override fun getTwitterAccessToken(verifier: String): Single<AccToken> {
        return Single.fromCallable { twitterClient.getOAuthAccessToken(verifier) }
            .map { response ->
                AccToken(response.token, response.tokenSecret)
            }
    }

    private fun defineRequestMethod(method: String): RequestMethod {
        return when(method){
            "GET" -> RequestMethod.GET
            "PUT" -> RequestMethod.PUT
            "DELETE" -> RequestMethod.DELETE
            "HEAD" -> RequestMethod.HEAD
            else -> throw Exception("unsupported request method")
        }
    }

    private fun convertParams(params: Array<HttpParam>): Array<HttpParameter> {
        val convertedParameters = ArrayList<HttpParameter>()
        params.forEach { it->
            convertedParameters.add(HttpParameter(it.param, it.value))
        }
        return convertedParameters.toTypedArray()
    }
}