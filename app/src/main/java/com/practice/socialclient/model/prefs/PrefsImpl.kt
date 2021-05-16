package com.practice.socialclient.model.prefs

import android.content.Context

class PrefsImpl(ctx: Context?) : BasicPrefsImpl(ctx!!), Prefs {
    override val defaultPrefsFileName: String
        get() = "socialNetwork"


    companion object {
        private const val KEY_TWITTER_LOGIN_STATE = "twitter_login_state"
        private const val KEY_FACEBOOK_LOGIN_STATE = "facebook_login_state"
        private const val KEY_FACEBOOK_USER_ICON = "facebook_user_icon"
        private const val KEY_TWITTER_AUTH_TOKEN = "twitter_auth_token"
        private const val KEY_TWITTER_AUTH_SECRET = "twitter_auth_secret"
        private const val KEY_FACEBOOK_USER_NAME = "facebook_user_name"
    }

    override fun putTwitterAuthToken(twitterAuthToken: String) {
        put(KEY_TWITTER_AUTH_TOKEN, twitterAuthToken)
    }

    override fun getTwitterAuthToken(): String {
        return get(KEY_TWITTER_AUTH_TOKEN, "")
    }

    override fun putTwitterAuthSecret(twitterAuthSecret: String) {
        put(KEY_TWITTER_AUTH_SECRET, twitterAuthSecret)
    }

    override fun getTwitterAuthSecret(): String {
        return get(KEY_TWITTER_AUTH_SECRET, "")
    }

    override fun putFbUserIcon(iconUrl: String?) {
        put(KEY_FACEBOOK_USER_ICON, iconUrl.toString())
    }

    override fun getFbUserIcon(): String {
        return get(KEY_FACEBOOK_USER_ICON, "")
    }

    override fun putIsFbLoggedIn(loginState: Boolean) {
        put(KEY_FACEBOOK_LOGIN_STATE, loginState)
    }

    override fun getIsFbLoggedIn(): Boolean {
        return get(KEY_FACEBOOK_LOGIN_STATE, false)
    }

    override fun putIsTwLoggedIn(loginState: Boolean) {
        put(KEY_TWITTER_LOGIN_STATE, loginState)
    }

    override fun getIsTwLoggedIn(): Boolean {
        return get(KEY_TWITTER_LOGIN_STATE, false)
    }

    override fun putFbUserName(userName: String) {
        put(KEY_FACEBOOK_USER_NAME, userName)
    }

    override fun getFbUserName(): String {
        return get(KEY_FACEBOOK_USER_NAME, "")
    }
}