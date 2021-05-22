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
import io.reactivex.Completable

class FacebookAuthRepositoryImpl(
    private val logger: Log,
    private val prefs: Prefs
) : FacebookAuthRepository {

    private lateinit var callbackManager: CallbackManager
    private var accToken: AccessToken? = null

    override fun isLoggedIn(): Boolean {
        return AccessToken.getCurrentAccessToken() != null // prefs.getIsFbLoggedIn()
    }

    override fun setupLoginState(): Completable {
        return Completable.fromAction {
            if (!isLoggedIn()) {
                registerFacebookCallBack()
            }
        }
    }

    override fun getPermissions(): List<String> {
        return listOf("user_status", "pages_read_user_content", "user_birthday", "user_friends")
    }

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

    override fun checkAuthState() {
        prefs.putIsFbLoggedIn(isLoggedIn())
    }

    override fun logOut() {
        logger.log("LogOutUtilImpl logOut()")
        prefs.putIsFbLoggedIn(false)
        prefs.putFbUserName("")
        prefs.putFbUserIcon("")
        LoginManager.getInstance().logOut()
    }
}
