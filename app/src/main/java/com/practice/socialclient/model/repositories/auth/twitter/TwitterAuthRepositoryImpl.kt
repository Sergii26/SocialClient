package com.practice.socialclient.model.repositories.auth.twitter

import android.net.Uri
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.model.repositories.network.twitter.TwitterNetworkClient
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import twitter4j.Twitter
import twitter4j.auth.AccessToken

class TwitterAuthRepositoryImpl(
        private val twitterClient: Twitter,
        private val twitterNetworkClient: TwitterNetworkClient,
        private val prefs: Prefs,
) : TwitterAuthRepository {

    private fun setTwitterAccToken(token: String, secret: String) {
        twitterClient.oAuthAccessToken = AccessToken(token, secret)
    }

    override fun setupLoginState(): Completable {
        if (isLoggedIn()) {
            setTwitterAccToken(prefs.getTwitterAuthToken(), prefs.getTwitterAuthSecret())
        }
        return twitterNetworkClient.isLoggedIn()
                .doOnComplete { prefs.putIsTwLoggedIn(true) }
                .doOnError { prefs.putIsTwLoggedIn(false) }
    }

    override fun isLoggedIn(): Boolean {
        return prefs.getTwitterAuthSecret().isNotEmpty()
    }

    override fun logOut() {
        prefs.putIsTwLoggedIn(false)
        prefs.putTwitterAuthSecret("")
        prefs.putTwitterAuthToken("")
        twitterClient.oAuthAccessToken = null
    }

    override fun getTwitterAuthUrl(): Maybe<String> {
        if (isLoggedIn()) {
            return Maybe.empty()
        }
        return Maybe.fromCallable { twitterClient.oAuthRequestToken }
                .map { response -> response.authorizationURL }
    }

    override fun getTwitterAccessToken(url: String): Completable {
        val uri = Uri.parse(url)
        val oauthVerifier = uri.getQueryParameter("oauth_verifier") ?: ""
        return Single.fromCallable { twitterClient.getOAuthAccessToken(oauthVerifier) }
                .map { response -> AccToken(response.token, response.tokenSecret) }
                .doOnSuccess { result ->
                    run {
                        prefs.putTwitterAuthToken(result.token)
                        prefs.putTwitterAuthSecret(result.secret)
                        setTwitterAccToken(result.token, result?.secret.toString())
                        prefs.putIsTwLoggedIn(true)
                    }
                }.ignoreElement()
    }
}