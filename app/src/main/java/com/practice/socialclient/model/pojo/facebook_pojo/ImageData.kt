package com.practice.socialclient.model.pojo.facebook_pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageData(
    @SerializedName("id") @Expose var id: String? = null,
    @SerializedName("images") @Expose var images: List<Image>? = null
)
