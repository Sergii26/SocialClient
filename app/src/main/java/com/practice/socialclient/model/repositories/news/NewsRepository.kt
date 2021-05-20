package com.practice.socialclient.model.repositories.news

import com.practice.socialclient.model.schemas.NewsInfo
import io.reactivex.Single

interface NewsRepository {
    fun getNews(limit: String): Single<List<NewsInfo>>
    fun getNextNewsPage(limit: String): Single<List<NewsInfo>>
    fun cleanCache()
}