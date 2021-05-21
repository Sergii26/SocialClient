package com.practice.socialclient.model.repositories.photos

import com.practice.socialclient.model.schemas.PhotoInfo
import io.reactivex.Single

interface PhotosRepository {
    fun getPhotos(limit: String): Single<List<PhotoInfo>>
    fun getNextPhotosPage(limit: String): Single<List<PhotoInfo>>
    fun cleanCache()
}