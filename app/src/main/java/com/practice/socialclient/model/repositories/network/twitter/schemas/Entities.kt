package com.practice.socialclient.model.repositories.network.twitter.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Entities(
    @SerializedName("media") @Expose var mediaArray: Array<Media?>
)
