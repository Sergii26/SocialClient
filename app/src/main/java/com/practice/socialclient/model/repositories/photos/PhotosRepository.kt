package com.practice.socialclient.model.repositories.photos

import io.reactivex.Single

interface PhotosRepository {
    fun getPhotos(limit: String): Single<List<String>>
    fun getNextPhotosPage(limit: String): Single<List<String>>
    fun cleanCache()
}