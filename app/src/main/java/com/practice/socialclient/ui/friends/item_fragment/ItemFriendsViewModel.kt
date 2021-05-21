package com.practice.socialclient.ui.friends.item_fragment

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.repositories.auth.AuthRepository
import com.practice.socialclient.model.dto.FriendInfo
import com.practice.socialclient.model.dto.FriendsCountInfo
import com.practice.socialclient.model.dto.UserInfo
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.model.repositories.friends.FriendsRepository
import com.practice.socialclient.model.repositories.user.UserInfoRepository
import com.practice.socialclient.model.utils_android.Utils
import com.practice.socialclient.ui.arch.MvvmViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ItemFriendsViewModel constructor(
    private val logger: Log,
    private val authRepository: AuthRepository,
    private val androidUtils: Utils,
    private val repository: FriendsRepository,
    private val twUserRepository: UserInfoRepository,
    private val fbUserRepository: UserInfoRepository,
    private val prefs: Prefs,
) : MvvmViewModel(), ItemFriendsContract.ViewModel {

    private val friendsCount = MutableLiveData<FriendsCountInfo>()
    private val turnOffRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    private val friendsList: MutableLiveData<MutableList<FriendInfo>> = MutableLiveData()
    private val internetState: MutableLiveData<Boolean> = MutableLiveData()
    private val friendsLimit = "20"
    private val fbUserInfoObservable: MutableLiveData<UserInfo> = MutableLiveData()
    private val twUserInfoObservable: MutableLiveData<UserInfo> = MutableLiveData()

    override fun getTwUserDataObservable(): LiveData<UserInfo> {
        return twUserInfoObservable
    }

    override fun getFbUserDataObservable(): LiveData<UserInfo> {
        return fbUserInfoObservable
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

    override fun logOut() {
        authRepository.logOut()
    }

    override fun getFriendsListObservable(): MutableLiveData<MutableList<FriendInfo>> {
        return friendsList
    }

    override fun clearFriendsList() {
        friendsList.value?.clear()
    }

    override fun getFriendsCount(): MutableLiveData<FriendsCountInfo> {
        return friendsCount
    }

    override fun getTernOffRefreshing(): MutableLiveData<Boolean> {
        return turnOffRefreshing
    }

    override fun getInternetState(): MutableLiveData<Boolean> {
        return internetState
    }

    override fun checkInternetConnection() {
        internetState.value = androidUtils.isConnectedToNetwork
    }

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event) {
        super.onAny(owner, event)
        if(event == Lifecycle.Event.ON_CREATE){
            checkInternetConnection()
            getFriends()
            getFriendsTotalCount()
        }
    }

    override fun onRefresh() {
        logger.log("ItemFriendsViewModel onRefresh()")
        repository.cleanCache()
        turnOffRefreshing.value = true
        if (androidUtils.isConnectedToNetwork) {
            getFriendsTotalCount()
            getFriends()
        } else {
            internetState.value = false
        }
    }

    override fun getFriendsTotalCount() {
        logger.log("ItemFriendsViewModel getFriendsCount()")
        if (!androidUtils.isConnectedToNetwork) {
            internetState.value = false
            return
        }
        internetState.value = true
        if (authRepository.isLoggedIn()) fetchFriendsCount()
    }

    private fun fetchFriendsCount() {
        compositeDisposable.add(repository.getFriendsCount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                logger.log("ItemFriendsViewModel getFbFriendsCount() - success")
                friendsCount.value = result
            }, { error ->
                logger.log("ItemFriendsViewModel getFbFriendsCount() error: ${error.message}")
            }
            ))
    }

    override fun getFriends() {
        logger.log("ItemFriendsViewModel getFriends()")
        if (!androidUtils.isConnectedToNetwork) {
            internetState.value = false
            return
        }
        internetState.value = true
        if (authRepository.isLoggedIn()) fetchFriends()
    }

    private fun fetchFriends() {
        compositeDisposable.add(
            repository.getFriends(friendsLimit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    logger.log("ItemFriendsViewModel getFbFriends() - success")
                    friendsList.value = result.toMutableList()
                }, { error ->
                    logger.log("ItemFriendsViewModel getFbFriends() error: ${error.message}")
                })
        )
    }

    override fun getNextFriendsPage() {
        logger.log("FriendsViewModel getNextFbPage()")
        if (!androidUtils.isConnectedToNetwork) {
            internetState.value = false
            return
        }
        internetState.value = true
        if(authRepository.isLoggedIn())fetchNextFriendsPage()
    }

    private fun fetchNextFriendsPage() {
        compositeDisposable.add(
            repository.getNextFriendsPage(friendsLimit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    logger.log("ItemFriendsViewModel getNextPage() - success")
                    friendsList.value = result.toMutableList()
                }, { error ->
                    logger.log("ItemFriendsViewModel getNextPage() error: ${error.message}")
                })
        )
    }

}
