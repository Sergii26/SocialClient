package com.practice.socialclient.model.repositories.photos.facebook

import com.facebook.AccessToken
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.repositories.network.facebook.FacebookNetworkClient
import com.practice.socialclient.model.repositories.photos.PhotosRepository
import com.practice.socialclient.model.dto.PhotoInfo
import io.reactivex.Single

class FacebookPhotosRepository(
    private val facebookNetworkClient: FacebookNetworkClient,
    private val logger: Log
) :
    PhotosRepository {

    private var fbCursor: String = ""

    override fun getPhotos(limit: String): Single<List<PhotoInfo>> {
        logger.log("FacebookPhotosRepository getPhotos()")
        return facebookNetworkClient.getUserPhotos(getFbToken(), limit)
            .map { response ->
                if (response[0].cursor.isEmpty() || response[0].cursor == "null") {
                    fbCursor = ""
                } else {
                    fbCursor = response[0].cursor
                }
                response
            }
    }

    override fun getNextPhotosPage(limit: String): Single<List<PhotoInfo>> {
        logger.log("FacebookPhotosRepository getNextPhotosPage()")
        if (fbCursor == "") {
            return Single.just(ArrayList())
        }
        return facebookNetworkClient.getNextUserPhotosPage(getFbToken(), fbCursor, limit)
            .map { response ->
                if (response[0].cursor.isEmpty() || response[0].cursor == "null") {
                    fbCursor = ""
                } else {
                    fbCursor = response[0].cursor
                }
                response
            }
    }

    override fun cleanCache() {
        fbCursor = ""
    }

    private fun getFbToken(): String {
        return AccessToken.getCurrentAccessToken().token
    }
}