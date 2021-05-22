package com.practice.socialclient.model.repositories.auth.twitter

import io.reactivex.Single

interface TwitterAuthRepository {
    fun getTwitterAuthUrl(): Single<String>
    fun getTwitterAccessToken(verifier: String): Single<AccToken>
    fun setTwitterAccToken(token: String, secret: String)
    fun cleanTwitterAccToken()
}