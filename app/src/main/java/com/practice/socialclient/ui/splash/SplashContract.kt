package com.practice.socialclient.ui.splash

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import com.practice.socialclient.ui.arch.Contract

interface SplashContract {
    interface BaseSplashViewModel : LifecycleObserver {
        fun startTimer()
        fun getIsLoggedIn(): LiveData<Boolean>
    }

    interface Host : Contract.Host {
        fun openLoginFragment()
        fun jumpToNewsFragment()
    }
}