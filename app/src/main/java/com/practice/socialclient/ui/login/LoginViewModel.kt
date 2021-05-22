package com.practice.socialclient.ui.login

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.repositories.auth.facebook.FacebookAuthRepository
import com.practice.socialclient.model.repositories.auth.twitter.TwitterAuthRepository
import com.practice.socialclient.ui.arch.MvvmViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val logger: Log,
    private val facebookAuthRepo: FacebookAuthRepository,
    private val twitterAuthRepo: TwitterAuthRepository
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
        return facebookAuthRepo.getPermissions()
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
        compositeDisposable.add(twitterAuthRepo.getTwitterAuthUrl()
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

    override fun handleUrl(url: String) {
        logger.log("LoginViewModel handleUrl()")
        compositeDisposable.add(twitterAuthRepo.getTwitterAccessToken(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, { error ->
                    logger.log("handleUrl() on error: " + error.message)
                }))
    }

    override fun launchFB() {
        logger.log("LoginViewModel launchFB()")
        compositeDisposable.add(facebookAuthRepo.setupLoginState().subscribe())
    }

    override fun getFaceBookRepo(): FacebookAuthRepository {
        return facebookAuthRepo
    }

    override fun checkLoginStates() {
        logger.log("LoginViewModel checkLoginStates()")
        facebookAuthRepo.checkAuthState()
        loginStateChecked.value = FB_LOGIN_CHECKED
        setTwLoginState()
    }

    private fun setTwLoginState() {
        logger.log("LoginViewModel setTwLoginState()")
        compositeDisposable.add(twitterAuthRepo.setupLoginState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (loginStateChecked.value.isNullOrEmpty()) {
                        loginStateChecked.value = TW_LOGIN_CHECKED
                    }
                    if (loginStateChecked.value == FB_LOGIN_CHECKED) {
                        loginStateChecked.value = LOGIN_CHECKED
                    }
                },
                        { error ->
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
