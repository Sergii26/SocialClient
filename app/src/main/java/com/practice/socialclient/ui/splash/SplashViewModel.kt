package com.practice.socialclient.ui.splash

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.repositories.auth.facebook.FacebookAuthRepository
import com.practice.socialclient.model.repositories.auth.twitter.TwitterAuthRepository
import com.practice.socialclient.ui.arch.MvvmViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashViewModel @Inject constructor(
        private val logger: Log,
        private val twitterAuthRepo: TwitterAuthRepository, // can be AuthRepository
        private val faceBookAuthRepo: FacebookAuthRepository // can be AuthRepository
) : MvvmViewModel(), SplashContract.ViewModel {

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
                        }, { throwable: Throwable -> logger.log("SplashViewModel startTimer() error: " + throwable.message) })
        )
    }

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event) {
        super.onAny(owner, event)
        if (event == Lifecycle.Event.ON_RESUME) {
            startTimer()
        }
    }

    private fun checkLoginStates() {
        compositeDisposable.add(twitterAuthRepo.setupLoginState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isLoggedIn.value = true
                },
                        { error ->
                            if (faceBookAuthRepo.isLoggedIn()) {
                                isLoggedIn.value = true
                            } else {
                                isNotLoggedIn.value = true
                            }
                            logger.log("isTwitterLoggedIn() on error: " + error.message)
                        }
                ))
    }
}
