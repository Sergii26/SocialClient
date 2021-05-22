package com.practice.socialclient.model.repositories.auth.facebook

import android.content.Intent
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.prefs.Prefs

class FacebookAuthRepositoryImpl(
    private val logger: Log,
    private val prefs: Prefs
) : FacebookAuthRepository {

    private lateinit var callbackManager: CallbackManager
    private var accToken: AccessToken? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun registerFacebookCallBack() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    accToken = loginResult?.accessToken
                    logger.log("FacebookFacebookLoginManager onSuccess")
                    prefs.putIsFbLoggedIn(true)
                }

                override fun onCancel() {
                    logger.log("FFacebookFacebookLoginManagerI onCancel")
                }

                override fun onError(exception: FacebookException) {
                    logger.log("FacebookFacebookLoginManager onError: ${exception.message}")
                }
            })
    }

    override fun logOut() {
        LoginManager.getInstance().logOut()
    }
}