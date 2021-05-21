package com.practice.socialclient.model.repositories.news.facebook

import com.facebook.AccessToken
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.repositories.network.facebook.FacebookNetworkClient
import com.practice.socialclient.model.repositories.network.facebook.schemas.NewsResponse
import com.practice.socialclient.model.repositories.network.facebook.schemas.UserDataResponse
import com.practice.socialclient.model.repositories.news.NewsRepository
import com.practice.socialclient.model.dto.NewsInfo
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FacebookNewsRepository(
    private val facebookNetworkClient: FacebookNetworkClient,
    private val logger: Log
) : NewsRepository {

    private var lastFbNewsId = 0L

    override fun getNews(limit: String): Single<List<NewsInfo>> {
        return facebookNetworkClient.getNews(getFbToken(), limit)
            .map { result ->
                val newsList = ArrayList<NewsInfo>()
                if (result.size > 20) {
                    newsList.addAll(result.subList(0, 20))
                    lastFbNewsId = result[20].createdAtUnix
                } else {
                    lastFbNewsId = 0L
                    newsList.addAll(result)
                }
                newsList
            }
    }

    override fun getNextNewsPage(limit: String): Single<List<NewsInfo>> {

        if (lastFbNewsId == 0L) {
            return Single.just(ArrayList())
        }
        return facebookNetworkClient.getNewsWithUntilTime(
            getFbToken(), limit,
            lastFbNewsId.toString()
        )
            .map { result ->
                val newsList = ArrayList<NewsInfo>()
                if (result.size > 20) {
                    newsList.addAll(result.subList(0, 20))
                    lastFbNewsId = result[20].createdAtUnix
                } else {
                    lastFbNewsId = 0L
                    newsList.addAll(result)
                }
                newsList
            }
    }

    override fun cleanCache() {
        lastFbNewsId = 0L
    }

    private fun getFbToken(): String {
        return AccessToken.getCurrentAccessToken().token
    }

    private fun convertFbNews(
        fbNews: NewsResponse,
        userData: UserDataResponse
    ): MutableList<NewsInfo> {
        val convertedNews: MutableList<NewsInfo> = ArrayList()
        val responseList = fbNews.newsData as ArrayList
        responseList.forEach {
            convertedNews.add(
                NewsInfo(
                    it?.id.toString(),
                    it?.message ?: "",
                    it?.attachments?.attachmentsData?.get(0)?.media?.image?.src.toString(),
                    userData.pictureData?.picture?.url.toString(),
                    userData.name.toString(),
                    it?.comments?.summary?.totalCount?.toString() ?: "0",
                    it?.reactions?.summary?.totalCount?.toString() ?: "0",
                    convertFbUTCTimeToUnix(it?.createdTime.toString()),
                    NewsInfo.SOURCE_FACEBOOK,
                    changeFbDatePattern(it?.createdTime.toString()),
                    fbNews.paging?.cursors?.after.toString()
                )
            )
        }
        return convertedNews
    }

    private fun convertFbUTCTimeToUnix(UTCTime: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH)
        val date = sdf.parse(UTCTime)
        return date.time / 1000
    }

    private fun changeFbDatePattern(UTCTime: String): String {
        val unixTime = convertFbUTCTimeToUnix(UTCTime).toLong()
        val date1 = Date(unixTime * 1000L)
        val sdf = SimpleDateFormat("MMMM dd, hh:mm aaa")
        val formattedDate = sdf.format(date1)
        return formattedDate
    }

}