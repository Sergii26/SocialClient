package com.practice.socialclient.model.repositories.network.facebook.schemas

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class NewsResponse(
    @SerializedName("data") @Expose var newsData: List<NewsData?>? = null,
    @SerializedName("paging") @Expose var paging: Paging? = null
)
