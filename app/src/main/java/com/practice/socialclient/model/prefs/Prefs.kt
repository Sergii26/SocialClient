package com.practice.socialclient.model.prefs

interface Prefs {
    fun putTwitterAuthToken(twitterAuthToken: String)
    fun getTwitterAuthToken(): String
    fun putTwitterAuthSecret(twitterAuthSecret: String)
    fun getTwitterAuthSecret(): String


    fun putFbUserIcon(iconUrl: String?)
    fun getFbUserIcon(): String
    fun putIsFbLoggedIn(loginState: Boolean)
    fun getIsFbLoggedIn(): Boolean
    fun putIsTwLoggedIn(loginState: Boolean)
    fun getIsTwLoggedIn(): Boolean
    fun putFbUserName(userName: String)
    fun getFbUserName(): String
}