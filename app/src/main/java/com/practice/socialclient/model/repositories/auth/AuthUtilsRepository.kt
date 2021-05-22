package com.practice.socialclient.model.repositories.auth

interface AuthUtilsRepository {
    fun isLoggedIn(): Boolean
    fun logOut()


}