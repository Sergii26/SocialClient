package com.practice.socialclient.model.repositories.photos.twitter

import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.network_api.twitter.TwitterNetworkClient
import com.practice.socialclient.model.repositories.photos.PhotosRepository
import io.reactivex.Single
import twitter4j.Twitter

class TwitterPhotosRepository(
    private val twitterNetworkClient: TwitterNetworkClient,
    private val logger: Log
) : PhotosRepository {

    private var lastTweetId = 0L

    override fun getPhotos(limit: String): Single<List<String>> {
        logger.log("TwitterPhotosRepository getPhotos")
        return twitterNetworkClient.getUserTweets(limit)
            .map { response ->
                val photos: MutableList<String> = ArrayList()
                response.forEach { it ->
                    if (it.entities?.mediaArray?.get(0)?.mediaUrl.toString() != "null") {
                        photos.add(it.entities?.mediaArray?.get(0)?.mediaUrl.toString())
                    }
                }
                if (response[response.size - 1].id != null) {
                    lastTweetId = response[response.size - 1].id!!
                }
                photos.subList(0, photos.size - 2)
            }
    }


    override fun getNextPhotosPage(limit: String): Single<List<String>> {
        logger.log("TwitterPhotosRepository getNextPhotosPage")
        if (lastTweetId == 0L) {
            logger.log("TwitterPhotosRepository getNextPhotosPage no next page")
            return Single.just(ArrayList())
        }
        return twitterNetworkClient.getUserTweetsOlderThan(lastTweetId, limit)
            .map { response ->
                val photosList: MutableList<String> = ArrayList()
                if (response.size == 1) {
                    lastTweetId = 0
                } else {
                    lastTweetId = response[response.size - 1].id!!
                    val photos: MutableList<String> = ArrayList()
                    response.forEach { it ->
                        if (it.entities?.mediaArray?.get(0)?.mediaUrl.toString() != "null") {
                            photos.add(it.entities?.mediaArray?.get(0)?.mediaUrl.toString())
                        }
                    }
                    //first element is part of previous page - skip it
                    photosList.addAll(photos.subList(1, photos.size))
                }
                photosList
            }
    }

    override fun cleanCache() {
        lastTweetId = 0L
    }

}