package com.practice.socialclient.model.repositories.news.twitter

import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.repositories.network.twitter.TwitterNetworkClient
import com.practice.socialclient.model.repositories.news.NewsRepository
import com.practice.socialclient.model.dto.NewsInfo
import io.reactivex.Single

class TwitterNewsRepository(
    private val twitterNetworkClient: TwitterNetworkClient,
    private val logger: Log
) : NewsRepository {

    private var lastTweetId = 0L

    override fun getNews(limit: String): Single<List<NewsInfo>> {
        logger.log("TwitterPhotosRepository getPhotos")
        return twitterNetworkClient.getTweets(limit)
            .map { response ->
                val newsList = ArrayList<NewsInfo>()
                if (response.size > 20) {
                    newsList.addAll(response.subList(0, 20))
                    lastTweetId = response[20].id.toLong()
                } else {
                    lastTweetId = 0L
                    newsList.addAll(response)
                }
                newsList
            }
    }

    override fun getNextNewsPage(limit: String): Single<List<NewsInfo>> {
        logger.log("TwitterPhotosRepository getNextNewsPage")
        if (lastTweetId == 0L) {
            return Single.just(ArrayList())
        }
        logger.log("TwitterPhotosRepository getNextNewsPage lastTweetId: $lastTweetId")
        return twitterNetworkClient.getTweetsOlderThan(lastTweetId, limit)
            .map { response ->
                val newsList = ArrayList<NewsInfo>()
                if (response.size > 20) {
                    newsList.addAll(response.subList(0, 20))
                    lastTweetId = response[20].id.toLong()
                } else {
                    newsList.addAll(response)
                    lastTweetId = 0L
                }
                newsList
            }
    }

    override fun cleanCache() {
        lastTweetId = 0L
    }

}