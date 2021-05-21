package com.practice.socialclient.model.repositories.network.facebook.schemas

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class AttachmentsData(
    @SerializedName("media") @Expose var media: Media? = null,
    @SerializedName("type") @Expose var type: String? = null,
    @SerializedName("url") @Expose var url: String? = null,
    @SerializedName("title") @Expose var title: String? = null
)
