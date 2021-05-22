package com.practice.socialclient.ui.news.item_fragment

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practice.socialclient.model.dto.NewsInfo
import com.practice.socialclient.model.dto.UserInfo
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.model.repositories.auth.AuthRepository
import com.practice.socialclient.model.repositories.news.NewsRepository
import com.practice.socialclient.model.repositories.user.UserInfoRepository
import com.practice.socialclient.model.utils.Utils
import com.practice.socialclient.ui.arch.MvvmViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ItemNewsViewModel(
        private val logger: Log,
        private val twUserRepository: UserInfoRepository,
        private val fbUserRepository: UserInfoRepository,
        private val newsRepository: NewsRepository,
        private val authRepository: AuthRepository,
        private val androidUtils: Utils,
        private val prefs: Prefs
) : MvvmViewModel(), ItemNewsContract.ViewModel {

    private val newsLimit = "25"
    private val newsList: MutableLiveData<MutableList<NewsInfo>> = MutableLiveData()
    private val internetState: MutableLiveData<Boolean> = MutableLiveData()
    private val turnOffRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    private val fbUserInfoObservable: MutableLiveData<UserInfo> = MutableLiveData()
    private val twUserInfoObservable: MutableLiveData<UserInfo> = MutableLiveData()

    override fun getTwUserDataObservable(): LiveData<UserInfo> {
        return twUserInfoObservable
    }

    override fun getFbUserDataObservable(): LiveData<UserInfo> {
        return fbUserInfoObservable
    }

    override fun getNewsListObservable(): MutableLiveData<MutableList<NewsInfo>> {
        return newsList
    }

    override fun getInternetStateObservable(): MutableLiveData<Boolean> {
        return internetState
    }

    override fun getTernOffRefreshingObservable(): MutableLiveData<Boolean> {
        return turnOffRefreshing
    }

    override fun checkInternetConnection() {
        internetState.value = androidUtils.isConnectedToNetwork
    }

    override fun clearNewsList() {
        newsList.value?.clear()
    }

    override fun logOut() {
        authRepository.logOut()
    }

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event) {
        super.onAny(owner, event)
        if(event == Lifecycle.Event.ON_CREATE){
            checkInternetConnection()
            getNews()
            getUserInfo()
        }
    }

    override fun onRefresh() {
        logger.log("NewsViewModel onRefresh")
        turnOffRefreshing.value = true
        if (androidUtils.isConnectedToNetwork) {
            internetState.value = true
            getNews()
        } else {
            internetState.value = false
        }
    }

    override fun getNews() {
//        setCurrentTwLoginState()
        logger.log("NewsViewModel getNews")
        if (!androidUtils.isConnectedToNetwork) {
            internetState.value = false
            return
        }
        internetState.value = true
        if (authRepository.isLoggedIn()) fetchNews()
    }

    private fun fetchNews() {
        logger.log("NewsViewModel fetchNews")
        compositeDisposable.add(
            newsRepository.getNews(newsLimit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                        newsList.value = result.toMutableList()
                },
                    { error -> logger.log("getFbNews() error: " + error?.message) })
        )
    }

    override fun getNextNewsPage() {
        logger.log("NewsViewModel getNextNewsPage")
        if (!androidUtils.isConnectedToNetwork) {
            internetState.value = true
            return
        }
        if (authRepository.isLoggedIn()) fetchNextNewsPage()
    }

    private fun fetchNextNewsPage() {
        logger.log("NewsViewModel getNextFbPage")
        compositeDisposable.add(
            newsRepository.getNextNewsPage(newsLimit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                        newsList.value = result.toMutableList()
                }, { error -> logger.log("NewsViewModel getNextFbPage error: " + error?.message) })
        )
    }

    override fun getUserInfo() {
        logger.log("NewsViewModel getNews")
        if (!androidUtils.isConnectedToNetwork) {
            internetState.value = false
            return
        }
        internetState.value = true
        if(prefs.getIsFbLoggedIn()){
            fetchFbUserInfo()
        } else {
            fbUserInfoObservable.value = UserInfo("","")
        }
        if(prefs.getIsTwLoggedIn()){
            fetchTwUserInfo()
        } else {
            twUserInfoObservable.value = UserInfo("","")
        }
    }

    private fun fetchTwUserInfo() {
        compositeDisposable.add(twUserRepository.getUserInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({result ->
                twUserInfoObservable.value = result
            }, { error ->
                logger.log("ItemNewsViewModel fetchTwUserInfo() error: ${error.message}")
                twUserInfoObservable.value = UserInfo("","")
            }))

    }

    private fun fetchFbUserInfo() {
        compositeDisposable.add(fbUserRepository.getUserInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({result ->
                fbUserInfoObservable.value = result
            }, { error ->
                logger.log("ItemNewsViewModel fetchFbUserInfo() error: ${error.message}")
                twUserInfoObservable.value = UserInfo("","")
            }))
    }

}