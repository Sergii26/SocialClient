package com.practice.socialclient.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.network_api.facebook.client.FacebookLoginManager
import com.practice.socialclient.model.network_api.twitter.TwitterNetworkClient
import com.practice.socialclient.model.network_api.twitter.client.TwitterClient
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.ui.arch.MvvmViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class LoginViewModel @Inject constructor(
    private val logger: Log,
    private val prefs: Prefs,
    private val twitterClient: TwitterClient,
    private val twitterNetworkClient: TwitterNetworkClient,
    private val facebookLoginManager: FacebookLoginManager
) : MvvmViewModel(), LoginContract.ViewModel {

    companion object {
        const val FB_LOGIN_CHECKED = "facebook_login_checked"
        const val TW_LOGIN_CHECKED = "twitter_login_checked"
        const val LOGIN_CHECKED = "login_checked"
    }

    private val twitterAuthUrl: MutableLiveData<String> = MutableLiveData()
    private val loginStateChecked: MutableLiveData<String> = MutableLiveData()

    override fun getLoginCheckingState(): MutableLiveData<String> {
        return loginStateChecked
    }


    override fun twitterAuthUrl(): LiveData<String> {
        return twitterAuthUrl
    }

    override fun getFbPermissions(): List<String> {
        return listOf(
            "user_status",
            "pages_read_user_content",
            "user_birthday",
            "user_friends",
        )
    }

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event) {
        super.onAny(owner, event)
        if (event == Lifecycle.Event.ON_CREATE) {
            launchFB()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getRequestToken() {
        logger.log("LoginViewModel getRequestToken()")
        if (prefs.getTwitterAuthSecret().isEmpty()) {
            compositeDisposable.add(twitterClient.getAuthUrl()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    twitterAuthUrl.value = result
                },
                    { error ->
                        logger.log("twitter authUrl error: " + error.message)
                    })
            )
        }
    }

    override fun handleUrl(url: String) {
        logger.log("LoginViewModel handleUrl()")
        val uri = Uri.parse(url)
        val oauthVerifier = uri.getQueryParameter("oauth_verifier") ?: ""

        compositeDisposable.add(twitterClient.getAccessToken(oauthVerifier)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ result ->
                prefs.putTwitterAuthToken(result.token)
                prefs.putTwitterAuthSecret(result.secret)
                twitterClient.setAccToken(result.token, result?.secret.toString())
                prefs.putIsTwLoggedIn(true)
            }, {error ->
                logger.log("handleUrl() on error: " + error.message)
            }))
    }

    override fun launchFB() {
        logger.log("LoginViewModel launchFB()")
        if (!checkFBState()) facebookLoginManager.registerCallBack()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebookLoginManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun checkLoginStates() {
        logger.log("LoginViewModel checkLoginStates()")
        prefs.putIsFbLoggedIn(checkFBState())
        loginStateChecked.value = FB_LOGIN_CHECKED
        setTwLoginState()
    }

    private fun checkFBState(): Boolean {
        logger.log("LoginViewModel checkFBState()")
        return com.facebook.AccessToken.getCurrentAccessToken() != null
    }

    private fun setTwLoginState() {
        logger.log("LoginViewModel setTwLoginState()")
        compositeDisposable.add(twitterNetworkClient.isLoggedIn()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                prefs.putIsTwLoggedIn(true)
                if (loginStateChecked.value.isNullOrEmpty()) {
                    loginStateChecked.value = TW_LOGIN_CHECKED
                }
                if (loginStateChecked.value == FB_LOGIN_CHECKED) {
                    loginStateChecked.value = LOGIN_CHECKED
                }
            },
                { error ->
                    prefs.putIsTwLoggedIn(false)
                    if (loginStateChecked.value.isNullOrEmpty()) {
                        loginStateChecked.value = TW_LOGIN_CHECKED
                    }
                    if (loginStateChecked.value == FB_LOGIN_CHECKED) {
                        loginStateChecked.value = LOGIN_CHECKED
                    }
                    logger.log("isTwitterLoggedIn() on error: " + error.message)
                }
            ))
    }

}