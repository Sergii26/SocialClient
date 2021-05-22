package com.practice.socialclient.model.repositories.network.twitter.header_factory

interface TwitterHeaderFactory {
    fun createAuthHeader(
        parameters: Array<HttpParam>,
        requestMethod: String,
        requestUrl: String
    ): String


}