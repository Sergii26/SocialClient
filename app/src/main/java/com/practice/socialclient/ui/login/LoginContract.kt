package com.practice.socialclient.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.CallbackManager
import com.practice.socialclient.ui.arch.FragmentContract

interface LoginContract {
    interface Host : FragmentContract.Host {
        fun openNewsFragment()
    }

    interface ViewModel : FragmentContract.ViewModel{

        fun checkLoginStates()
        fun getLoginCheckingState(): LiveData<String>
        fun getRequestToken()
        fun twitterAuthUrl(): LiveData<String>
        fun handleUrl(url: String)
        fun launchFB()
        fun getCallBackManager(): CallbackManager
        fun getFbPermissions(): List<String>
    }
}