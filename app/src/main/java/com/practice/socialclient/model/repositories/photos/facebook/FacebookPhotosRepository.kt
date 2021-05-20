package com.practice.socialclient.model.repositories.photos.facebook

import com.facebook.AccessToken
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.network_api.facebook.FacebookNetworkClient
import com.practice.socialclient.model.repositories.photos.PhotosRepository
import io.reactivex.Single

class FacebookPhotosRepository(
    private val facebookNetworkClient: FacebookNetworkClient,
    private val logger: Log
) :
    PhotosRepository {

    private var fbCursor: String = ""

    override fun getPhotos(limit: String): Single<List<String>> {
        logger.log("FacebookPhotosRepository getPhotos()")
        return facebookNetworkClient.getUserPhotos(getFbToken(), limit)
            .map { response ->
                fbCursor =
                    if (response.paging?.next.isNullOrEmpty()) "" else response.paging?.cursors?.after.toString()
                val photos: MutableList<String> = ArrayList()
                response.data?.forEach { it ->
                    photos.add(
                        it.images?.get(0)?.src.toString()
                    )
                }
                photos
            }
    }

    override fun getNextPhotosPage(limit: String): Single<List<String>> {
        logger.log("FacebookPhotosRepository getNextPhotosPage()")
        if (fbCursor == "") {
            return Single.just(ArrayList())
        }
        return facebookNetworkClient.getNextUserPhotosPage(getFbToken(), fbCursor, limit)
            .map { response ->
                fbCursor =
                    if (response.paging?.next.isNullOrEmpty()) "" else response.paging?.cursors?.after.toString()
                val photos: MutableList<String> = ArrayList()
                response.data?.forEach { it ->
                    photos.add(
                        it.images?.get(0)?.src.toString()
                    )
                }
                photos
            }
    }

    override fun cleanCache() {
        fbCursor = ""
    }

    private fun getFbToken(): String {
        return AccessToken.getCurrentAccessToken().token
    }
}