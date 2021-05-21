package com.practice.socialclient.model.repositories.news

import com.practice.socialclient.model.dto.NewsInfo
import io.reactivex.Single

interface NewsRepository {
    fun getNews(limit: String): Single<List<NewsInfo>>
    fun getNextNewsPage(limit: String): Single<List<NewsInfo>>
    fun cleanCache()
}