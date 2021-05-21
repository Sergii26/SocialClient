package com.practice.socialclient.ui.splash

import androidx.lifecycle.*
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.network_api.twitter.TwitterNetworkClient
import com.practice.socialclient.model.network_api.twitter.client.TwitterClient
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.ui.arch.MvvmViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class SplashViewModel @Inject constructor(
    private val logger: Log,
    private val twitterClient: TwitterClient,
    private val prefs: Prefs,
    private val twitterNetworkClient: TwitterNetworkClient
) : MvvmViewModel(),
    SplashContract.ViewModel {

    private val isLoggedIn = MutableLiveData<Boolean>()
    private val isNotLoggedIn = MutableLiveData<Boolean>()

    override fun getIsLoggedInObservable(): LiveData<Boolean> {
        return isLoggedIn;
    }

    override fun getIsNotLoggedInObservable(): LiveData<Boolean> {
        return isNotLoggedIn
    }

    override fun startTimer() {
        compositeDisposable.add(
            Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    checkLoginStates()
                },
                    { throwable: Throwable -> logger.log("SplashViewModel startTimer() error: " + throwable.message) })
        )

    }

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event) {
        super.onAny(owner, event)
        if (event == Lifecycle.Event.ON_RESUME) {
            startTimer()
        }
    }

    private fun setCurrentTwLoginState() {
        if (prefs.getTwitterAuthSecret().isNotEmpty()) {
            twitterClient.setAccToken(prefs.getTwitterAuthToken(), prefs.getTwitterAuthSecret())
        }
    }

    private fun checkLoginStates() {
        setCurrentTwLoginState()
        compositeDisposable.add(twitterNetworkClient.isLoggedIn()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                logger.log("checkLoginStates() result: $result")
                prefs.putIsTwLoggedIn(true)
                isLoggedIn.value = true
            },
                { error ->
                    prefs.putIsTwLoggedIn(false)
                    if(prefs.getIsFbLoggedIn()){
                        isLoggedIn.value = true
                    } else {
                        isNotLoggedIn.value = true
                    }
                    logger.log("isTwitterLoggedIn() on error: " + error.message)
                }
            ))
    }

}
