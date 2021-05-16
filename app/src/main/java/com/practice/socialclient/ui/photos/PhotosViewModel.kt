package com.practice.socialclient.ui.photos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.practice.socialclient.model.logger.ILog
import com.practice.socialclient.model.network_api.facebook.FacebookNetworkClient
import com.practice.socialclient.model.network_api.twitter.TwitterNetworkClient
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.model.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import twitter4j.Twitter

class PhotosViewModel(
    private val logger: ILog, private val facebookNetworkClient: FacebookNetworkClient,
    private val twitterNetworkClient: TwitterNetworkClient,
    private val twitterClient: Twitter, private val prefs: Prefs, private val androidUtils: Utils
) :
    ViewModel(), Contract.BaseViewModel {
    private val compositeDisposable = CompositeDisposable()
    private val twUserPhotos: MutableLiveData<MutableList<String>> = MutableLiveData()
    private val fbUserPhotos: MutableLiveData<MutableList<String>> = MutableLiveData()
    private val turnOffRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    private val internetState: MutableLiveData<Boolean> = MutableLiveData()
    private val photosLimit = 10
    private var fbCursor: String = ""
    private var lastTweetId: Long = 0

    override fun getTwUserPhotos(): MutableLiveData<MutableList<String>> {
        return twUserPhotos
    }

    override fun getFbUserPhotos(): MutableLiveData<MutableList<String>> {
        return fbUserPhotos
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

    override fun clearTwPhotosList() {
        twUserPhotos.value?.clear()
    }

    override fun clearFbPhotosList() {
        fbUserPhotos.value?.clear()
    }

    override fun downloadPhotos() {
        logger.log("PhotosViewModel downloadPhotos")
        if (!androidUtils.isConnectedToNetwork) {
            internetState.value = false
            return
        }
        if (prefs.getIsFbLoggedIn()) getFbPhotos()
        if (prefs.getIsTwLoggedIn()) getTwPhotos()
    }

    private fun getFbPhotos() {
        logger.log("PhotosViewModel getFbPhotos")
        compositeDisposable.add(
            facebookNetworkClient.getUserPhotos(
                AccessToken.getCurrentAccessToken().token,
                photosLimit.toString()
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        fbCursor =
                            if (result.paging?.next.isNullOrEmpty()) "" else result.paging?.cursors?.after.toString()
                        val photos: MutableList<String> = ArrayList()
                        result.data?.forEach { it ->
                            photos.add(
                                it.images?.get(0)?.src.toString()
                            )
                        }
                        fbUserPhotos.value = photos
                    },
                    { error -> logger.log("PhotosViewModel testMethodGetPhotosFb() error: ${error.message}") })
        )

    }

    private fun getTwPhotos() {
        logger.log("PhotosViewModel getTwPhotos")
        compositeDisposable.add(
            twitterNetworkClient.getUserTweets(photosLimit.toString(), twitterClient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    val photos: MutableList<String> = ArrayList()
                    result.forEach { it ->
                        if (it.entities?.mediaArray?.get(0)?.mediaUrl.toString() != "null") {
                            photos.add(it.entities?.mediaArray?.get(0)?.mediaUrl.toString())
                        }
                    }
                    if (result[result.size - 1].id != null) {
                        lastTweetId = result[result.size - 1].id!!
                    }
                    twUserPhotos.value = photos.subList(0, photos.size - 2)
                },
                    { error -> logger.log("getTwPhotos() error: " + error?.message) })
        )
    }

    override fun downloadNextFbPage() {
        logger.log("PhotosViewModel downloadNextFbPage")
        if (!androidUtils.isConnectedToNetwork) {
            internetState.value = false
            return
        }
        if (fbCursor == "") {
            return
        }
        compositeDisposable.add(
            facebookNetworkClient.getNextUserPhotosPage(
                AccessToken.getCurrentAccessToken().token, fbCursor,
                photosLimit.toString()
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        fbCursor =
                            if (result.paging?.next.isNullOrEmpty()) "" else result.paging?.cursors?.after.toString()
                        val photos: MutableList<String> = ArrayList()
                        result.data?.forEach { it ->
                            photos.add(
                                it.images?.get(0)?.src.toString()
                            )
                        }
                        fbUserPhotos.value = photos
                    },
                    { error -> logger.log("PhotosViewModel testMethodGetPhotosFb() error: ${error.message}") })
        )
    }

    override fun downloadNextTwPage() {
        logger.log("PhotosViewModel downloadNextTwPage")
        if (!androidUtils.isConnectedToNetwork) {
            internetState.value = false
            return
        }

        if (lastTweetId == 0L) {
            return
        }
        compositeDisposable.add(
            twitterNetworkClient.getUserTweetsOlderThan(
                lastTweetId,
                photosLimit.toString(),
                twitterClient
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    logger.log("tw photos next page result size: ${result.size}")
                    if (result.size == 1) {
                        lastTweetId = 0
                    } else {
                        lastTweetId = result[result.size - 1].id!!
                        val photos: MutableList<String> = ArrayList()
                        result.forEach { it ->
                            if (it.entities?.mediaArray?.get(0)?.mediaUrl.toString() != "null") {
                                photos.add(it.entities?.mediaArray?.get(0)?.mediaUrl.toString())
                            }
                        }
                        //first element is part of last added page - skip it
                        twUserPhotos.value = photos.subList(1, photos.size)
                    }
                }, { error -> logger.log("getSortedNews error: " + error?.message) })
        )
    }

    override fun onRefresh() {
        logger.log("PhotosViewModel onRefresh")
        fbCursor = ""
        lastTweetId = 0
        turnOffRefreshing.value = true
        if (androidUtils.isConnectedToNetwork) {
            downloadPhotos()
        } else {
            internetState.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}