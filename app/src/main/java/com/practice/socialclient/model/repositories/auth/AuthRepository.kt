package com.practice.socialclient.model.repositories.auth

import io.reactivex.Completable

interface AuthRepository {
    fun setupLoginState(): Completable
    fun isLoggedIn(): Boolean
    fun logOut()
}