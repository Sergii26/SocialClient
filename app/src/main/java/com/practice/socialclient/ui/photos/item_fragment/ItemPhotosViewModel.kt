package com.practice.socialclient.ui.photos.item_fragment

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.repositories.auth.AuthRepository
import com.practice.socialclient.model.dto.UserInfo
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.model.repositories.photos.PhotosRepository
import com.practice.socialclient.model.repositories.user.UserInfoRepository
import com.practice.socialclient.model.dto.PhotoInfo
import com.practice.socialclient.model.utils_android.Utils
import com.practice.socialclient.ui.arch.MvvmViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ItemPhotosViewModel(
    private val logger: Log,
    private val authRepository: AuthRepository,
    private val androidUtils: Utils,
    private val repository: PhotosRepository,
    private val twUserRepository: UserInfoRepository,
    private val fbUserRepository: UserInfoRepository,
    private val prefs: Prefs,
) : MvvmViewModel(), ItemPhotosContract.ViewModel {
    private val photosLimit = "20"
    private val photosList: MutableLiveData<MutableList<PhotoInfo>> = MutableLiveData()
    private val turnOffRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    private val internetState: MutableLiveData<Boolean> = MutableLiveData()
    private val fbUserInfoObservable: MutableLiveData<UserInfo> = MutableLiveData()
    private val twUserInfoObservable: MutableLiveData<UserInfo> = MutableLiveData()

    override fun getTwUserDataObservable(): LiveData<UserInfo> {
        return twUserInfoObservable
    }

    override fun getFbUserDataObservable(): LiveData<UserInfo> {
        return fbUserInfoObservable
    }

    override fun getPhotosObservable(): MutableLiveData<MutableList<PhotoInfo>> {
        return photosList
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

    override fun clearPhotosList() {
        photosList.value?.clear()
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

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event) {
        super.onAny(owner, event)
        if(event == Lifecycle.Event.ON_CREATE){
            checkInternetConnection()
            getPhotos()
        }
    }

    override fun getPhotos() {
        logger.log("PhotosViewModel downloadPhotos")
        if (!androidUtils.isConnectedToNetwork) {
            internetState.value = false
            return
        }
        if (authRepository.isLoggedIn()) fetchPhotos()

    }

    private fun fetchPhotos() {
        logger.log("PhotosViewModel getFbPhotos")
        compositeDisposable.add(repository.getPhotos(photosLimit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        photosList.value = result.toMutableList()
                    },
                    { error -> logger.log("PhotosViewModel testMethodGetPhotosFb() error: ${error.message}") })
        )
    }


    override fun getNextPhotosPage() {
        logger.log("PhotosViewModel downloadNextFbPage")
        if (!androidUtils.isConnectedToNetwork) {
            internetState.value = false
            return
        }
        if (authRepository.isLoggedIn()) fetchNextPhotosPage()
    }

    private fun fetchNextPhotosPage() {
        compositeDisposable.add(
            repository.getNextPhotosPage(photosLimit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        photosList.value = result.toMutableList()
                    },
                    { error -> logger.log("PhotosViewModel testMethodGetPhotosFb() error: ${error.message}") })
        )
    }

    override fun onRefresh() {
        logger.log("PhotosViewModel onRefresh")
        repository.cleanCache()
        turnOffRefreshing.value = true
        if (androidUtils.isConnectedToNetwork) {
            getPhotos()
        } else {
            internetState.value = false
        }
    }

}