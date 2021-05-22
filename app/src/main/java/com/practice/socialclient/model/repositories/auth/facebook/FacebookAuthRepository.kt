package com.practice.socialclient.model.repositories.auth.facebook

import android.content.Intent
import com.practice.socialclient.model.repositories.auth.AuthRepository

interface FacebookAuthRepository : AuthRepository {
    fun getPermissions(): List<String>
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    fun registerFacebookCallBack()
    fun checkAuthState()
}