package com.practice.socialclient.model.repositories.auth.twitter

import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.repositories.network.facebook.client.FacebookLoginManager
import com.practice.socialclient.model.repositories.network.twitter.client.TwitterClient
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.model.repositories.auth.AuthRepository

class TwitterAuthRepository(
    private val logger: Log,
    private val twitterClient: TwitterClient,
    private val prefs: Prefs,
    private val fBFacebookLoginManager: FacebookLoginManager
) : AuthRepository {

    override fun isLoggedIn(): Boolean {
        return prefs.getIsTwLoggedIn()
    }

    override fun logOut() {
        logger.log("LogOutUtilImpl logOut()")
        prefs.putIsTwLoggedIn(false)
        prefs.putTwitterAuthSecret("")
        prefs.putTwitterAuthToken("")
        twitterClient.cleanAccToken()
        prefs.putIsFbLoggedIn(false)
        prefs.putFbUserName("")
        prefs.putFbUserIcon("")
        fBFacebookLoginManager.LogOut()
    }
}