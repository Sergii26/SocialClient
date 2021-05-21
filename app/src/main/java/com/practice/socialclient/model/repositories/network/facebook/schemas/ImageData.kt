package com.practice.socialclient.model.repositories.network.facebook.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageData(
    @SerializedName("id") @Expose var id: String? = null,
    @SerializedName("images") @Expose var images: List<Image>? = null
)
