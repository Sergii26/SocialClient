package com.practice.socialclient.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.socialclient.model.logger.ILog
import com.practice.socialclient.model.network_api.twitter.TwitterNetworkClient
import com.practice.socialclient.model.prefs.Prefs
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import twitter4j.Twitter
import twitter4j.auth.AccessToken
import java.util.concurrent.TimeUnit

class SplashViewModel(
    private val logger: ILog, private val twitterClient: Twitter, private val prefs: Prefs,
    private val twitterNetworkClient: TwitterNetworkClient
) : ViewModel(), SplashContract.BaseSplashViewModel {
    private val isLoggedIn = MutableLiveData<Boolean>()
    private val compositeDisposable = CompositeDisposable()

    override fun getIsLoggedIn(): LiveData<Boolean> {
        return isLoggedIn;
    }

    override fun startTimer() {
        compositeDisposable.add(
            Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ checkLoginStates() },
                    { throwable: Throwable -> logger.log("SplashViewModel startTimer() error: " + throwable.message) })
        )
    }

    private fun setCurrentTwLoginState() {
        if (prefs.getTwitterAuthSecret().isNotEmpty()) {
            twitterClient.oAuthAccessToken =
                AccessToken(prefs.getTwitterAuthToken(), prefs.getTwitterAuthSecret())
        }
    }

    private fun checkLoginStates() {
        setCurrentTwLoginState()
        compositeDisposable.add(twitterNetworkClient.isLoggedIn(twitterClient)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                logger.log("checkLoginStates() result: $result")
                prefs.putIsTwLoggedIn(true)
                isLoggedIn.value = true
            },
                { error ->
                    prefs.putIsTwLoggedIn(false)
                    isLoggedIn.value = prefs.getIsFbLoggedIn()
                    logger.log("isTwitterLoggedIn() on error: " + error.message)
                }
            ))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
