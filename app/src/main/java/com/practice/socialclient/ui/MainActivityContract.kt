package com.practice.socialclient.ui

import androidx.lifecycle.MutableLiveData
import com.practice.socialclient.model.pojo.UserInfo

interface MainActivityContract {
    interface BaseMainActivityViewModel {
        fun getFbUserData(): MutableLiveData<UserInfo>
        fun getTwUserData(): MutableLiveData<UserInfo>
        fun getUserData()
        fun logOut()
        fun getInternetState(): MutableLiveData<Boolean>
        fun checkInternetConnection()
    }

    interface Host
}
