package com.practice.socialclient.ui.login

import androidx.lifecycle.LiveData
import com.practice.socialclient.model.repositories.auth.facebook.FacebookAuthRepository
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
        fun getFbPermissions(): List<String>
        fun getFaceBookRepo(): FacebookAuthRepository
    }
}