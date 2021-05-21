package com.practice.socialclient.ui.friends.item_fragment

import androidx.lifecycle.LiveData
import com.google.android.material.navigation.NavigationView
import com.practice.socialclient.model.dto.FriendInfo
import com.practice.socialclient.model.dto.FriendsCountInfo
import com.practice.socialclient.model.dto.UserInfo
import com.practice.socialclient.ui.arch.FragmentContract

interface ItemFriendsContract {

    interface ViewModel : FragmentContract.ViewModel{
        fun getFriends()
        fun getNextFriendsPage()
        fun getFriendsCount(): LiveData<FriendsCountInfo>
        fun getFriendsTotalCount()
        fun onRefresh()
        fun getTernOffRefreshing(): LiveData<Boolean>
        fun getInternetState(): LiveData<Boolean>
        fun getFriendsListObservable(): LiveData<MutableList<FriendInfo>>
        fun clearFriendsList()
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
