package com.practice.socialclient.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.practice.socialclient.model.logger.ILog
import com.practice.socialclient.model.network_api.facebook.FacebookNetworkClient
import com.practice.socialclient.model.network_api.twitter.TwitterNetworkClient
import com.practice.socialclient.model.pojo.*
import com.practice.socialclient.model.pojo.facebook_pojo.UserDataResponse
import com.practice.socialclient.model.pojo.twitter_pojo.User
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.model.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import twitter4j.Twitter
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val logger: ILog, private val facebookNetworkClient: FacebookNetworkClient,
    private val twitterNetworkClient: TwitterNetworkClient, private val prefs: Prefs, private val twitterClient: Twitter,
    private val androidUtils: Utils
) :
    ViewModel(), MainActivityContract.BaseMainActivityViewModel {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val fbUserData = MutableLiveData<UserInfo>()
    private val twUserData = MutableLiveData<UserInfo>()
    private val internetState = MutableLiveData<Boolean>()

    override fun getFbUserData(): MutableLiveData<UserInfo> {
        return fbUserData
    }

    override fun getTwUserData(): MutableLiveData<UserInfo> {
        return twUserData
    }

    override fun getInternetState(): MutableLiveData<Boolean>{
        return internetState
    }

    override fun checkInternetConnection() {
        internetState.value = androidUtils.isConnectedToNetwork
    }

    override fun getUserData() {
        if(!androidUtils.isConnectedToNetwork){
            internetState.value = false
            return
        }
        internetState.value = true
        if (prefs.getIsFbLoggedIn()) {
            downloadFbUserData()
        } else {
            fbUserData.value = UserInfo("","")
        }
        if (prefs.getIsTwLoggedIn()) {
            downloadTwUserData()
        } else {
            twUserData.value = UserInfo("","")
        }
    }

    override fun logOut() {
        logger.log("MainActivityViewModel logOut()")
        prefs.putIsTwLoggedIn(false)
        prefs.putTwitterAuthSecret("")
        prefs.putTwitterAuthToken("")
        twitterClient.oAuthAccessToken = null
        prefs.putIsFbLoggedIn(false)
        prefs.putFbUserName("")
        prefs.putFbUserIcon("")
        LoginManager.getInstance().logOut()
    }

    private fun downloadFbUserData() {
        logger.log("MainActivityViewModel downloadFbUserData()")
        if(AccessToken.getCurrentAccessToken().token.isNullOrEmpty()){
            prefs.putIsFbLoggedIn(false)
            prefs.putFbUserName("")
            prefs.putFbUserIcon("")
            return
        }
        compositeDisposable.add(facebookNetworkClient.getUserData(AccessToken.getCurrentAccessToken().token)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response: UserDataResponse ->
                prefs.putFbUserIcon(response.pictureData?.picture?.url.toString())
                prefs.putFbUserName(response.name.toString())
                fbUserData.value = UserInfo(
                    response.name.toString(),
                    response.pictureData?.picture?.url.toString()
                )
            })
            { throwable: Throwable ->
                logger.log("downloadFbUserData error: " + throwable.message)
            })
    }

    private fun downloadTwUserData() {
        logger.log("MainActivityViewModel downloadTwUserData()")
        logger.log("MainActivityViewModel twitter hashCode: ${twitterClient.hashCode()}")
//        logger.log("MainActivityViewModel downloadTwUserData() twitterClient.oAuth2Token = ${twitterClient.oAuth2Token}")
//        logger.log("MainActivityViewModel downloadTwUserData() twitterClient.oAuthAccessToken = ${twitterClient.oAuthAccessToken}")
//        logger.log("MainActivityViewModel downloadTwUserData() twitterClient.authorization = ${twitterClient.authorization.toString()}")

        compositeDisposable.add(twitterNetworkClient.getUserData(twitterClient)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response: User ->
                logger.log("MainActivityViewModel downloadTwUserData() user name: ${response.name.toString()}, " +
                        "user icon: ${response.profileImageUrlHttps.toString()}")
                twUserData.value = UserInfo(
                    response.name.toString(),
                    response.profileImageUrlHttps.toString()
                )
            })
            { throwable: Throwable ->
                logger.log("downloadTwUserData error: " + throwable.message)
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}