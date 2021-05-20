package com.practice.socialclient.model.utils_login.twitter

import com.practice.socialclient.model.utils_login.LoginStateUtil
import com.practice.socialclient.model.prefs.Prefs

class TwitterLoginStateUtil(private val prefs: Prefs): LoginStateUtil {
    override fun isLoggedIn(): Boolean {
        return prefs.getIsTwLoggedIn()
    }
}