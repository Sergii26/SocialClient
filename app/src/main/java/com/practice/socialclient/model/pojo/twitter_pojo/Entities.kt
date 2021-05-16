package com.practice.socialclient.model.pojo.twitter_pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Entities(
    @SerializedName("media") @Expose var mediaArray: Array<Media?>
)
