package com.practice.socialclient.model.repositories.network.facebook.schemas

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("height") @Expose var height: Int? = null,
    @SerializedName("source") @Expose var src: String? = null,
    @SerializedName("width") @Expose var width: Int? = null
)
