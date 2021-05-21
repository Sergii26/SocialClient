package com.practice.socialclient.model.repositories.photos.twitter

import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.repositories.network.twitter.TwitterNetworkClient
import com.practice.socialclient.model.repositories.photos.PhotosRepository
import com.practice.socialclient.model.dto.PhotoInfo
import io.reactivex.Single

class TwitterPhotosRepository(
    private val twitterNetworkClient: TwitterNetworkClient,
    private val logger: Log
) : PhotosRepository {

    private var lastTweetId = 0L

    override fun getPhotos(limit: String): Single<List<PhotoInfo>> {
        logger.log("TwitterPhotosRepository getPhotos")
        return twitterNetworkClient.getPhotos(limit)
            .map { response ->
                if (response[response.size - 1].cursor.isNotEmpty() && response[response.size - 1].cursor != "null") {
                    lastTweetId = response[response.size - 1].cursor.toLong()
                }
                response.subList(0, response.size - 2)
            }
    }


    override fun getNextPhotosPage(limit: String): Single<List<PhotoInfo>> {
        logger.log("TwitterPhotosRepository getNextPhotosPage")
        if (lastTweetId == 0L) {
            logger.log("TwitterPhotosRepository getNextPhotosPage no next page")
            return Single.just(ArrayList())
        }
        return twitterNetworkClient.getPhotosOlderThan(lastTweetId, limit)
            .map { response ->
                val photosList: MutableList<PhotoInfo> = ArrayList()
                if (response.size == 1) {
                    lastTweetId = 0
                } else {
                    if (response[response.size - 1].cursor.isNotEmpty() && response[response.size - 1].cursor != "null") {
                        lastTweetId = response[response.size - 1].cursor.toLong()
                    }
                    photosList.addAll(response.subList(0, response.size - 2))
                }
                photosList.toMutableList()
            }
    }

    override fun cleanCache() {
        lastTweetId = 0L
    }

}