package com.practice.socialclient.ui.photos.item_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.navigation.NavigationView
import com.practice.socialclient.model.dto.PhotoInfo
import com.practice.socialclient.model.dto.UserInfo

import com.practice.socialclient.ui.arch.FragmentContract

interface ItemPhotosContract {
    interface ViewModel : FragmentContract.ViewModel{
        fun getPhotos()
        fun getNextPhotosPage()
        fun clearPhotosList()
        fun getPhotosObservable(): MutableLiveData<MutableList<PhotoInfo>>
        fun onRefresh()
        fun getTernOffRefreshing(): LiveData<Boolean>
        fun getInternetState(): LiveData<Boolean>
        fun checkInternetConnection()
        fun getTwUserDataObservable(): LiveData<UserInfo>
        fun getFbUserDataObservable(): LiveData<UserInfo>
        fun logOut()
        fun getUserInfo()
    }

    interface Host : FragmentContract.Host {
        fun setTwUserData(user: UserInfo)
        fun setFbUserData(user: UserInfo)
        fun setNavListener(listener: NavigationView.OnNavigationItemSelectedListener)
        fun moveToFriendsFragment()
        fun moveToPhotosFragment()
        fun moveToNewsFragment()
        fun moveToLoginFragment()
        fun hideNavigationMenu()
        fun closeDrawer()
    }
}