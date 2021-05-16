package com.practice.socialclient.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.CallbackManager
import com.practice.socialclient.ui.arch.Contract

interface LoginContract {
    interface Host: Contract.Host {
        fun openNewsFragment()
    }
    interface BaseViewModel{

        fun checkLoginStates()
        fun getLoginCheckingState(): MutableLiveData<String>
        fun getRequestToken()
        fun twitterAuthUrl(): LiveData<String>
        fun handleUrl(url: String)
        fun launchFB()
        fun getCallBackManager(): CallbackManager
        fun getFbPermissions(): List<String>
    }
}