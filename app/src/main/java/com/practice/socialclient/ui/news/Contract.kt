package com.practice.socialclient.ui.news

import androidx.lifecycle.MutableLiveData
import com.practice.socialclient.model.pojo.NewsInfo
import com.practice.socialclient.ui.arch.Contract

interface Contract {
    interface BaseViewModel {
        fun getNewsList(): MutableLiveData<MutableList<NewsInfo>>
        fun downloadNews()
        fun getNextPage()
        fun getInternetState(): MutableLiveData<Boolean>
        fun onRefresh()
        fun getTernOffRefreshing() : MutableLiveData<Boolean>
        fun checkInternetConnection()
        fun clearNewsList()
    }
    interface Host : Contract.Host{
        fun downloadUserData()
    }
}