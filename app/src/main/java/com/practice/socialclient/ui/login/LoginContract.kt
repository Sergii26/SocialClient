package com.practice.socialclient.ui.login

import android.content.Intent
import androidx.lifecycle.LiveData

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
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    }
}