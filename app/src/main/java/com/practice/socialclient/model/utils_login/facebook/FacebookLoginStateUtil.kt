package com.practice.socialclient.model.utils_login.facebook

import com.practice.socialclient.model.utils_login.LoginStateUtil
import com.practice.socialclient.model.prefs.Prefs

class FacebookLoginStateUtil(private val prefs: Prefs): LoginStateUtil {
    override fun isLoggedIn(): Boolean {
        return prefs.getIsFbLoggedIn()
    }

}