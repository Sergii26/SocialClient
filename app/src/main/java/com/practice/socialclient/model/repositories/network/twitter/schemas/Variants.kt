package com.practice.socialclient.model.repositories.network.twitter.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Variants(
    @SerializedName("bitrate") @Expose var bitrate: Int?,
    @SerializedName("content_type") @Expose var contentType: String?,
    @SerializedName("url") @Expose var url: String?
)
