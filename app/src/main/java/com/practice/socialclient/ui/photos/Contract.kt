package com.practice.socialclient.ui.photos

import androidx.lifecycle.MutableLiveData

import com.practice.socialclient.ui.arch.Contract

interface Contract {
    interface BaseViewModel {
        fun downloadPhotos()
        fun downloadNextFbPage()
        fun downloadNextTwPage()
        fun getFbUserPhotos(): MutableLiveData<MutableList<String>>
        fun getTwUserPhotos(): MutableLiveData<MutableList<String>>
        fun onRefresh()
        fun getTernOffRefreshing(): MutableLiveData<Boolean>
        fun getInternetState(): MutableLiveData<Boolean>
        fun checkInternetConnection()
        fun clearTwPhotosList()
        fun clearFbPhotosList()
    }

    interface Host : Contract.Host {
        fun downloadUserData()
    }
}