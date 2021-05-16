package com.practice.socialclient.ui.friends

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.practice.socialclient.model.logger.ILog
import com.practice.socialclient.model.network_api.facebook.FacebookNetworkClient
import com.practice.socialclient.model.network_api.twitter.TwitterNetworkClient
import com.practice.socialclient.model.pojo.FriendInfo
import com.practice.socialclient.model.pojo.FriendsCountInfo
import com.practice.socialclient.model.pojo.facebook_pojo.UserFriendsResponse
import com.practice.socialclient.model.pojo.twitter_pojo.User
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.model.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import twitter4j.Twitter

class FriendsViewModel(
    private val logger: ILog, private val facebookNetworkClient: FacebookNetworkClient,
    private val twitterNetworkClient: TwitterNetworkClient, private val twitterClient: Twitter,
    private val prefs: Prefs, private val androidUtils: Utils
) :
    ViewModel(),
    Contract.BaseViewModel {

    private val compositeDisposable = CompositeDisposable()
    private val fbFriendsList = MutableLiveData<MutableList<FriendInfo>>()
    private val twFriendsList = MutableLiveData<MutableList<FriendInfo>>()
    private val friendsCount = MutableLiveData<FriendsCountInfo>()
    private val turnOffRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    private val internetState: MutableLiveData<Boolean> = MutableLiveData()
    private val friendsLimit = "20"
    private val zeroFriendsLimit = "0"
    private var friendsTotalCount = FriendsCountInfo("0", "0")
    private var twCursor: String = ""
    private var fbCursor: String = ""

    override fun getFriendsCount(): MutableLiveData<FriendsCountInfo> {
        return friendsCount
    }

    override fun getFbFriendsList(): MutableLiveData<MutableList<FriendInfo>> {
        return fbFriendsList
    }

    override fun getTwFriendsList(): MutableLiveData<MutableList<FriendInfo>> {
        return twFriendsList
    }

    override fun getTernOffRefreshing(): MutableLiveData<Boolean> {
        return turnOffRefreshing
    }

    override fun getInternetState(): MutableLiveData<Boolean> {
        return internetState
    }

    override fun clearFbFriendsList() {
        fbFriendsList.value?.clear()
    }

    override fun clearTwFriendsList() {
        twFriendsList.value?.clear()
    }

    override fun checkInternetConnection() {
        internetState.value = androidUtils.isConnectedToNetwork
    }

    override fun getFriendsTotalCount() {
        logger.log("FriendsViewModel getFriendsCount()")
        if (!androidUtils.isConnectedToNetwork) {
            internetState.value = false
            return
        }
        internetState.value = true
        val isFbLoggedIn = prefs.getIsFbLoggedIn()
        val isTwLoggedIn = prefs.getIsTwLoggedIn()
        if (isFbLoggedIn) {
            getFbFriendsCount()
        }
        if (isTwLoggedIn) {
            getTwFriendsCount()
        }
    }

    private fun getFbFriendsCount() {
        logger.log("FriendsViewModel getFbFriendsCount()")
        compositeDisposable.add(
            facebookNetworkClient.getUserFriends(getFbToken(), zeroFriendsLimit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: UserFriendsResponse ->
                    friendsTotalCount.fbFriendsCount = response.summary?.totalCount.toString()
                    friendsCount.value = friendsTotalCount
                },
                    { error: Throwable ->
                        logger.log("getFbFriendsCount() error: " + error.message)
                    })
        )
    }

    private fun getTwFriendsCount() {
        logger.log("FriendsViewModel getTwFriendsCount()")
        compositeDisposable.add(
            twitterNetworkClient.getFriendsCount(twitterClient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    friendsTotalCount.twFriendsCount = response.friendsCount.toString()
                    friendsCount.value = friendsTotalCount
                },
                    { error: Throwable ->
                        logger.log("getFbFriendsCount() error: " + error.message)
                    })
        )
    }

    override fun getFriends() {
        logger.log("FriendsViewModel getFriends()")
        if (!androidUtils.isConnectedToNetwork) {
            internetState.value = false
            return
        }
        internetState.value = true
        if (prefs.getIsFbLoggedIn()) getFbFriends()
        if (prefs.getIsTwLoggedIn()) getTwFriends()
    }

    private fun getFbFriends() {
        logger.log("FriendsViewModel getFbFriends()")
        compositeDisposable.add(
            facebookNetworkClient.getUserFriends(getFbToken(), friendsLimit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: UserFriendsResponse ->
                    logger.log("GetFbFriends() friends size ${response.data?.size}")
                    logger.log("GetFbFriends() friends next ${response.paging?.next}")
                    fbCursor = if (response.paging?.next == null) {
                        ""
                    } else {
                        response.paging?.cursors?.after.toString()
                    }
                    if (response.data?.size != 0) {
                        logger.log(response.data?.get(0)?.name)
                        logger.log(response.data?.get(0)?.picture?.data1te?.url.toString())
                        fbFriendsList.value = convertFbFriendsResponse(response)
                    }
                },
                    { error: Throwable ->
                        logger.log("GetFbFriends() error: " + error.message)
                    })
        )
    }


    private fun getTwFriends() {
        logger.log("FriendsViewModel getTwFriends()")
        compositeDisposable.add(
            twitterNetworkClient.getFriends(friendsLimit, twitterClient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    twCursor = if (result.nextCursorStr == "0") {
                        ""
                    } else {
                        result.nextCursorStr
                    }
                    twFriendsList.value = convertTwFriendsResponse(result.friends)
                }, { error: Throwable -> logger.log("getTwFriends() error: " + error.message) })
        )
    }

    override fun getNextFbPage() {
        logger.log("FriendsViewModel getNextFbPage()")
        if (!androidUtils.isConnectedToNetwork) {
            internetState.value = false
            return
        }
        if (fbCursor == "") {
            return
        }
        internetState.value = true

        compositeDisposable.add(
            facebookNetworkClient.getNextFriendsPage(getFbToken(), fbCursor, friendsLimit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: UserFriendsResponse ->
                    fbCursor = if (response.paging?.next.isNullOrEmpty()) {
                        ""
                    } else {
                        response.paging?.cursors?.after.toString()
                    }
                    if (response.data?.size != 0) {
                        logger.log(response.data?.get(0)?.name)
                        logger.log(response.data?.get(0)?.picture?.data1te?.url.toString())
                        fbFriendsList.value = convertFbFriendsResponse(response)
                    }
                },
                    { error: Throwable ->
                        logger.log("GetFbFriends() error: " + error.message)
                    })
        )
    }

    override fun getNextTwPage() {
        logger.log("FriendsViewModel getNextTwPage()")
        if (!androidUtils.isConnectedToNetwork) {
            internetState.value = false
            return
        }
        if (twCursor == "") {
            return
        }
        internetState.value = true

        compositeDisposable.add(
            twitterNetworkClient.getNextFriendsPage(friendsLimit, twCursor, twitterClient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    twCursor = result.nextCursorStr
                    twFriendsList.value = convertTwFriendsResponse(result.friends)
                }, { error: Throwable -> logger.log("getTwFriends() error: " + error.message) })
        )
    }

    override fun onRefresh() {
        logger.log("FriendsViewModel onRefresh()")
        twCursor = ""
        fbCursor = ""
        turnOffRefreshing.value = true
        if (androidUtils.isConnectedToNetwork) {
            getFriendsTotalCount()
            getFriends()
        } else {
            internetState.value = false
        }
    }

    private fun convertFbFriendsResponse(response: UserFriendsResponse): MutableList<FriendInfo> {
        val dataResponse = response.data
        val convertedResponse: MutableList<FriendInfo> = ArrayList()
        dataResponse?.forEach {
            convertedResponse.add(
                FriendInfo(
                    it.name.toString(),
                    it.picture?.data1te?.url.toString(),
                    FriendInfo.SOURCE_FACEBOOK
                )
            )
        }
        return convertedResponse
    }

    private fun convertTwFriendsResponse(response: Array<User>): MutableList<FriendInfo> {
        val convertedResponse: MutableList<FriendInfo> = ArrayList()
        response.forEach {
            convertedResponse.add(
                FriendInfo(
                    it.name.toString(),
                    it.profileImageUrlHttps.toString(),
                    FriendInfo.SOURCE_TWITTER
                )
            )
        }
        return convertedResponse
    }

    private fun getFbToken(): String {
        return AccessToken.getCurrentAccessToken().token
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
