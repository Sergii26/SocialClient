package com.practice.socialclient.model.repositories.news.twitter

import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.network_api.twitter.TwitterNetworkClient
import com.practice.socialclient.model.network_api.twitter.schemas.TweetsResponse
import com.practice.socialclient.model.schemas.NewsInfo
import com.practice.socialclient.model.repositories.news.NewsRepository
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TwitterNewsRepository(
    private val twitterNetworkClient: TwitterNetworkClient,
    private val logger: Log
) : NewsRepository {

    private var lastTweetId = 0L

    override fun getNews(limit: String): Single<List<NewsInfo>> {
        logger.log("TwitterPhotosRepository getPhotos")
        return twitterNetworkClient.getTweets(limit)
            .map { response ->
                val responseAsNewsInfo = convertTwNews(response)
                val newsList = ArrayList<NewsInfo>()
                if (responseAsNewsInfo.size > 20) {
                    newsList.addAll(responseAsNewsInfo.subList(0, 20))
                    lastTweetId = responseAsNewsInfo[20].id.toLong()
                } else {
                    lastTweetId = 0L
                    newsList.addAll(responseAsNewsInfo)
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
                val responseAsNewsInfo = convertTwNews(response)
                val newsList = ArrayList<NewsInfo>()
                if (responseAsNewsInfo.size > 20) {
                    newsList.addAll(responseAsNewsInfo.subList(0, 20))
                    lastTweetId = responseAsNewsInfo[20].id.toLong()
                } else {
                    newsList.addAll(responseAsNewsInfo)
                    lastTweetId = 0L
                }
                logger.log("TwitterPhotosRepository getNextNewsPage lastTweetId: $lastTweetId")
                newsList
            }

    }

    override fun cleanCache() {
        lastTweetId = 0L
    }

    private fun convertTwNews(twNewResponses: Array<TweetsResponse>): MutableList<NewsInfo> {
        val convertedNews: MutableList<NewsInfo> = ArrayList()
//        changeTwDatePattern(twNewResponses.get(0).createdAt.toString())
        twNewResponses.forEach {
            convertedNews.add(
                NewsInfo(
                    it.id.toString(),
                    it.fullText.toString(),
                    it.entities?.mediaArray?.get(0)?.mediaUrl.toString(),
                    it.user?.profileImageUrlHttps.toString(),
                    it.user?.name.toString(),
                    it.likeCount.toString(),
                    it.retweetCount.toString(),
                    convertTwUTCTimeToUnix(it.createdAt.toString()),
                    NewsInfo.SOURCE_TWITTER,
                    changeTwDatePattern(it.createdAt.toString())
                )
            )
        }
        return convertedNews
    }

    private fun changeTwDatePattern(UTCTime: String): String {
        val unixTime = convertTwUTCTimeToUnix(UTCTime).toLong()
        val date1 = Date(unixTime * 1000L)
        val sdf = SimpleDateFormat("MMMM dd, hh:mm aaa")
        return sdf.format(date1)
    }

    private fun convertTwUTCTimeToUnix(UTCTime: String): Long {
        val sdf = SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.ENGLISH)
        val date = sdf.parse(UTCTime)
        return date.time / 1000
    }
}