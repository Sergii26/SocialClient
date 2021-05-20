package com.practice.socialclient.model.utils_login

import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.network_api.twitter.client.TwitterClient
import com.practice.socialclient.model.prefs.Prefs

class LogOutUtilImpl(
    private val logger: Log,
    private val twitterClient: TwitterClient,
    private val prefs: Prefs,
    private val fBLoginManager: com.practice.socialclient.model.network_api.facebook.LoginManager
): LogOutUtil {
    override fun logOut() {
        logger.log("LogOutUtilImpl logOut()")
        prefs.putIsTwLoggedIn(false)
        prefs.putTwitterAuthSecret("")
        prefs.putTwitterAuthToken("")
        twitterClient.cleanAccToken()
        prefs.putIsFbLoggedIn(false)
        prefs.putFbUserName("")
        prefs.putFbUserIcon("")
        fBLoginManager.getLoginManager().logOut()
    }
}