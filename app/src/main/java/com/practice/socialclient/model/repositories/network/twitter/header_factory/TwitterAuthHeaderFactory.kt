package com.practice.socialclient.model.repositories.network.twitter.header_factory

import twitter4j.HttpParameter
import twitter4j.HttpRequest
import twitter4j.RequestMethod
import twitter4j.Twitter

class TwitterAuthHeaderFactory(private val twitterClient: Twitter) : TwitterHeaderFactory {

    override fun createAuthHeader(
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
        val convertedParameters = ArrayList<HttpParameter>()
        params.forEach { it->
            convertedParameters.add(HttpParameter(it.param, it.value))
        }
        return convertedParameters.toTypedArray()
    }
}