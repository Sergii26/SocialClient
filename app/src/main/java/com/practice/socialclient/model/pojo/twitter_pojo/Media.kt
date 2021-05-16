package com.practice.socialclient.model.pojo.twitter_pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Media(
    @SerializedName("media_url_https") @Expose var mediaUrl: String?,
    @SerializedName("type") @Expose var type: String?,
    @SerializedName("video_info") @Expose var videoInfo: VideoInfo?,

    )
