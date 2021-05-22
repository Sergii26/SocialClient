package com.practice.socialclient.model.repositories.auth.twitter

import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.model.repositories.auth.AuthUtilsRepository
import com.practice.socialclient.model.repositories.auth.facebook.FacebookAuthRepository

class TwitterAuthUtilsRepository(
    private val logger: Log,
    private val twitterAuthRepo: TwitterAuthRepository,
    private val prefs: Prefs,
    private val facebookAuthRepo: FacebookAuthRepository
) : AuthUtilsRepository {

    override fun isLoggedIn(): Boolean {
        return prefs.getIsTwLoggedIn()
    }

    override fun logOut() {
        logger.log("LogOutUtilImpl logOut()")
        prefs.putIsTwLoggedIn(false)
        prefs.putTwitterAuthSecret("")
        prefs.putTwitterAuthToken("")
        twitterAuthRepo.cleanTwitterAccToken()
        prefs.putIsFbLoggedIn(false)
        prefs.putFbUserName("")
        prefs.putFbUserIcon("")
        facebookAuthRepo.logOut()
    }

}