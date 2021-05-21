package com.practice.socialclient.model.network_api.facebook.client

import android.content.Intent

interface FacebookLoginManager {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    fun registerCallBack()
    fun LogOut()
}