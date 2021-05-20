package com.practice.socialclient.model.network_api.twitter.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoInfo(
    @SerializedName("variants") @Expose var variants: Array<Variants?>
)
