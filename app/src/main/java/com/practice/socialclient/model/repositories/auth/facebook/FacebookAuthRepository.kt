package com.practice.socialclient.model.repositories.auth.facebook

import android.content.Intent

interface FacebookAuthRepository {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    fun registerFacebookCallBack()
    fun logOut()
}