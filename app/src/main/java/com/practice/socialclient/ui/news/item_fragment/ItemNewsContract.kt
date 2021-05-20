package com.practice.socialclient.ui.news.item_fragment

import androidx.lifecycle.LiveData
import com.google.android.material.navigation.NavigationView
import com.practice.socialclient.model.schemas.NewsInfo
import com.practice.socialclient.model.schemas.UserInfo
import com.practice.socialclient.ui.arch.FragmentContract

interface ItemNewsContract {
    interface ViewModel : FragmentContract.ViewModel{
        fun getNewsListObservable(): LiveData<MutableList<NewsInfo>>
        fun getTwUserDataObservable(): LiveData<UserInfo>
        fun getFbUserDataObservable(): LiveData<UserInfo>
        fun getNews()
        fun getNextNewsPage()
        fun getInternetStateObservable(): LiveData<Boolean>
        fun onRefresh()
        fun getTernOffRefreshingObservable(): LiveData<Boolean>
        fun checkInternetConnection()
        fun clearNewsList()
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