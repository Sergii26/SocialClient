package com.practice.socialclient.model.network_api.facebook

class FacebookLoginManager: LoginManager {
    override fun getLoginManager(): com.facebook.login.LoginManager {
        return com.facebook.login.LoginManager.getInstance()
    }
}