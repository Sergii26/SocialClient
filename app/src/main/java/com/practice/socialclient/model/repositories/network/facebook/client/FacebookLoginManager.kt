package com.practice.socialclient.model.repositories.network.facebook.client

import android.content.Intent

interface FacebookLoginManager {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    fun registerCallBack()
    fun LogOut()
}