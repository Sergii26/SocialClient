package com.practice.socialclient.model.pojo.facebook_pojo

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class PhotoData(
    @SerializedName("id") @Expose var id: String? = null,
    @SerializedName("source") @Expose var images: List<Image>? = null
)
