package com.practice.socialclient.ui.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.practice.socialclient.model.logger.ILog
import com.practice.socialclient.model.network_api.facebook.FacebookNetworkClient
import com.practice.socialclient.model.network_api.twitter.TwitterNetworkClient
import com.practice.socialclient.model.pojo.NewsInfo
import com.practice.socialclient.model.pojo.facebook_pojo.NewsResponse
import com.practice.socialclient.model.pojo.twitter_pojo.TweetsResponse
import com.practice.socialclient.model.prefs.Prefs
import com.practice.socialclient.model.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import twitter4j.Twitter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewsViewModel(
    private val logger: ILog, private val facebookNetworkClient: FacebookNetworkClient,
    private val twitterNetworkClient: TwitterNetworkClient, private val twitterClient: Twitter,
    private val prefs: Prefs, private val androidUtils: Utils
) : ViewModel(), Contract.BaseViewModel {

    private val compositeDisposable = CompositeDisposable()
    private val newsList: MutableLiveData<MutableList<NewsInfo>> = MutableLiveData()
    private val internetState: MutableLiveData<Boolean> = MutableLiveData()
    private val turnOffRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    private var nextTwNews: NewsInfo = NewsInfo()
    private var nextFbNews: NewsInfo = NewsInfo()
    private val newsLimit = 25

    override fun getNewsList(): MutableLiveData<MutableList<NewsInfo>> {
        return newsList
    }

    override fun getInternetState(): MutableLiveData<Boolean> {
        return internetState
    }

    override fun getTernOffRefreshing(): MutableLiveData<Boolean> {
        return turnOffRefreshing
    }

    override fun checkInternetConnection() {
        internetState.value = androidUtils.isConnectedToNetwork
    }

    override fun clearNewsList() {
        newsList.value?.clear()
    }

    override fun onRefresh() {
        logger.log("NewsViewModel onRefresh")
        this.nextFbNews = NewsInfo()
        this.nextTwNews = NewsInfo()
        turnOffRefreshing.value = true
        if (androidUtils.isConnectedToNetwork) {
            internetState.value = true
            downloadNews()
        } else {
            internetState.value = false
        }
    }

    override fun downloadNews() {
        logger.log("NewsViewModel downloadNews")
        if (!androidUtils.isConnectedToNetwork) {
            internetState.value = false
            return
        }
        internetState.value = true
        val isFbLoggedIn = prefs.getIsFbLoggedIn()
        val isTwLoggedIn = prefs.getIsTwLoggedIn()
        if (isFbLoggedIn && isTwLoggedIn) {
            getBothNews()
        } else {
            if (isFbLoggedIn) {
                getFbNews()
            }
            if (isTwLoggedIn) {
                getTwNews()
            }
        }
    }

    private fun getFbNews() {
        logger.log("NewsViewModel getFbNews")
        compositeDisposable.add(
            facebookNetworkClient.getNews(getFbToken(), newsLimit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    val convertedFbNews = convertFbNews(result)
                    if (convertedFbNews.size > 20) {
                        newsList.value = convertedFbNews.subList(0, 20)
                        nextFbNews = convertedFbNews[20]
                    } else {
                        newsList.value = convertedFbNews
                    }
                },
                    { error -> logger.log("getFbNews() error: " + error?.message) })
        )
    }

    private fun getTwNews() {
        logger.log("NewsViewModel getTwNews")
        compositeDisposable.add(
            twitterNetworkClient.getTweets(newsLimit.toString(), twitterClient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    val convertedTwNews = convertTwNews(result)
                    if (convertedTwNews.size > 20) {
                        newsList.value = convertedTwNews.subList(0, 20)
                        nextTwNews = convertedTwNews[0]
                    } else {
                        newsList.value = convertedTwNews
                    }
                },
                    { error -> logger.log("getFbNews() error: " + error?.message) })
        )
    }

    private fun getBothNews() {
        logger.log("NewsViewModel getBothNews")
        compositeDisposable.add(
            twitterNetworkClient.getTweets(newsLimit.toString(), twitterClient)
                .zipWith(
                    facebookNetworkClient.getNews(getFbToken(), newsLimit),
                    { twNews, fbNews ->
                        val convertedFbNews = convertFbNews(fbNews)
                        val convertedTwNews = convertTwNews(twNews)
                        convertedFbNews.addAll(convertedTwNews)
                        convertedFbNews
                    })
                .map { news -> sortNewsByTime(news) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ news ->
                    nextTwNews = findNextTwNews(news)
                    nextFbNews = findNextFbNEws(news)
                    newsList.value = news.subList(0, 20)
                }, { error -> logger.log("getBothNews error: " + error?.message) })
        )

    }

    override fun getNextPage() {
        logger.log("NewsViewModel getNextPage")
        if (!androidUtils.isConnectedToNetwork) {
            internetState.value = true
            return
        }
        val isFbLoggedIn = prefs.getIsFbLoggedIn()
        val isTwLoggedIn = prefs.getIsTwLoggedIn()

        if (isFbLoggedIn && isTwLoggedIn) {
            getNextBothPage()
        } else {
            if (isFbLoggedIn) {
                getNextFbPage()
            }
            if (isTwLoggedIn) {
                getNextTwPage()
            }
        }
    }

    private fun getNextFbPage() {
        logger.log("NewsViewModel getNextFbPage")
        if (nextFbNews.id == "") {
            return
        }
        compositeDisposable.add(
            facebookNetworkClient.getNewsWithUntilTime(
                getFbToken(), newsLimit,
                nextFbNews.createdAtUnix.toString()
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    val convertedFbNews = convertFbNews(result)
                    if (convertedFbNews.size > 20) {
                        newsList.value = convertedFbNews.subList(0, 20)
                        nextFbNews = convertedFbNews[20]
                    } else {
                        newsList.value = convertedFbNews
                        nextFbNews.id = ""
                    }
                }, { error -> logger.log("getSortedNews error: " + error?.message) })
        )
    }

    private fun getNextTwPage() {
        logger.log("NewsViewModel getNextTwPage")
        if (nextTwNews.id == "") {
            return
        }

        compositeDisposable.add(
            twitterNetworkClient.getTweetsOlderThan(
                nextTwNews.id.toLong(), newsLimit.toString(), twitterClient
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    val convertedTwNews = convertTwNews(result)
                    if (convertedTwNews.size > 20) {
                        newsList.value = convertedTwNews.subList(0, 20)
                        nextTwNews = convertedTwNews[0]
                    } else {
                        newsList.value = convertedTwNews
                        nextTwNews.id = ""
                    }
                }, { error -> logger.log("getSortedNews error: " + error?.message) })
        )
    }

    private fun getNextBothPage() {
        logger.log("NewsViewModel getNextBothPage")
        compositeDisposable.add(
            twitterNetworkClient.getTweetsOlderThan(
                nextTwNews.id.toLong(),
                newsLimit.toString(),
                twitterClient
            )
                .zipWith(
                    facebookNetworkClient.getNewsWithUntilTime(
                        getFbToken(), newsLimit,
                        nextFbNews.createdAtUnix.toString()
                    ),
                    { twNewResponses: Array<TweetsResponse>, fbNews ->
                        val convertedFbNews = convertFbNews(fbNews)
                        val convertedTwNews = convertTwNews(twNewResponses)
                        val bothNews: MutableList<NewsInfo> = ArrayList()
                        bothNews.addAll(convertedFbNews)
                        bothNews.addAll(convertedTwNews)
                        bothNews
                    })
                .map { news ->
                    sortNewsByTime(news)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ news ->
                    nextTwNews = findNextTwNews(news)
                    nextFbNews = findNextFbNEws(news)
                    newsList.value = news.subList(0, 20)
                }, { error -> logger.log("getSortedNews error: " + error?.message) })
        )
    }

    private fun findNextTwNews(newsList: MutableList<NewsInfo>): NewsInfo {
        for (i in 20 until newsList.size) {
            if (newsList[i].source == NewsInfo.SOURCE_TWITTER) {
                return newsList[i]
            }
        }
        return NewsInfo()
    }

    private fun findNextFbNEws(newsList: MutableList<NewsInfo>): NewsInfo {
        for (i in 20 until newsList.size) {
            if (newsList[i].source == NewsInfo.SOURCE_FACEBOOK) {
                return newsList[i]
            }
        }
        return NewsInfo()
    }

    private fun sortNewsByTime(newsForSorting: MutableList<NewsInfo>): MutableList<NewsInfo> {
        newsForSorting.sortByDescending { it.createdAtUnix }
        return newsForSorting
    }

    private fun getFbToken(): String {
        return AccessToken.getCurrentAccessToken().token
    }

    private fun convertFbNews(fbNews: NewsResponse): MutableList<NewsInfo> {
        val convertedNews: MutableList<NewsInfo> = ArrayList()
        val responseList = fbNews.newsData as ArrayList
        val fbUserIcon = prefs.getFbUserIcon()
        val fbUserName = prefs.getFbUserName()
        responseList.forEach {
            convertedNews.add(
                NewsInfo(
                    it?.id.toString(),
                    it?.message ?: "",
                    it?.attachments?.attachmentsData?.get(0)?.media?.image?.src.toString(),
                    fbUserIcon,
                    fbUserName,
                    it?.comments?.summary?.totalCount?.toString() ?: "0",
                    it?.reactions?.summary?.totalCount?.toString() ?: "0",
                    convertFbUTCTimeToUnix(it?.createdTime.toString()),
                    NewsInfo.SOURCE_FACEBOOK,
                    changeFbDatePattern(it?.createdTime.toString())
                )
            )
        }
        return convertedNews
    }

    private fun convertTwNews(twNewResponses: Array<TweetsResponse>): MutableList<NewsInfo> {
        val convertedNews: MutableList<NewsInfo> = ArrayList()
        changeTwDatePattern(twNewResponses.get(0).createdAt.toString())
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
        val formattedDate = sdf.format(date1)
        return formattedDate
    }

    private fun changeFbDatePattern(UTCTime: String): String {
        val unixTime = convertFbUTCTimeToUnix(UTCTime).toLong()
        val date1 = Date(unixTime * 1000L)
        val sdf = SimpleDateFormat("MMMM dd, hh:mm aaa")
        val formattedDate = sdf.format(date1)
        return formattedDate
    }

    private fun convertTwUTCTimeToUnix(UTCTime: String): Long {
        val sdf = SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.ENGLISH)
        val date = sdf.parse(UTCTime)
        return date.time / 1000
    }


    private fun convertFbUTCTimeToUnix(UTCTime: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH)
        val date = sdf.parse(UTCTime)
        return date.time / 1000
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}