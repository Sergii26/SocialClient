package com.practice.socialclient.model.repositories.auth

interface AuthRepository {
    fun isLoggedIn(): Boolean
    fun logOut()
}