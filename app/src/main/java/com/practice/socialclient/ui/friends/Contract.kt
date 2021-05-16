package com.practice.socialclient.ui.friends

import androidx.lifecycle.MutableLiveData
import com.practice.socialclient.model.pojo.FriendInfo
import com.practice.socialclient.model.pojo.FriendsCountInfo
import com.practice.socialclient.ui.arch.Contract

interface Contract {
    interface BaseViewModel{
        fun getFriends()
        fun getFbFriendsList(): MutableLiveData<MutableList<FriendInfo>>
        fun getTwFriendsList(): MutableLiveData<MutableList<FriendInfo>>
        fun getFriendsCount(): MutableLiveData<FriendsCountInfo>
        fun clearFbFriendsList()
        fun clearTwFriendsList()
        fun getFriendsTotalCount()
        fun getNextFbPage()
        fun getNextTwPage()
        fun onRefresh()
        fun getTernOffRefreshing() : MutableLiveData<Boolean>
        fun getInternetState(): MutableLiveData<Boolean>
        fun checkInternetConnection()

    }
    interface Host : Contract.Host{
        fun downloadUserData()
    }
}