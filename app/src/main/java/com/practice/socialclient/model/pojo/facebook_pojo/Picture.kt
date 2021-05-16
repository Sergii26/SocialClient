package com.practice.socialclient.model.pojo.facebook_pojo

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class Picture(
    @SerializedName("is_silhouette") @Expose var isSilhouette: Boolean? = null,
    @SerializedName("url") @Expose var url: String? = null
)
