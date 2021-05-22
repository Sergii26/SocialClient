package com.practice.socialclient.model.repositories.auth.twitter

import com.practice.socialclient.model.repositories.auth.AuthRepository
import io.reactivex.Completable
import io.reactivex.Maybe

interface TwitterAuthRepository : AuthRepository {
    fun getTwitterAuthUrl(): Maybe<String>
    fun getTwitterAccessToken(url: String): Completable
}
