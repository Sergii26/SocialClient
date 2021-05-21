package com.practice.socialclient.ui.splash

import androidx.lifecycle.LiveData
import com.practice.socialclient.ui.arch.FragmentContract

interface SplashContract {
    interface ViewModel : FragmentContract.ViewModel {
        fun startTimer()
        fun getIsLoggedInObservable(): LiveData<Boolean>
        fun getIsNotLoggedInObservable(): LiveData<Boolean>
    }

    interface Host : FragmentContract.Host {
        fun openLoginFragment()
        fun jumpToNewsFragment()
    }
}