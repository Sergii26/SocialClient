package com.practice.socialclient.model.repositories.network.facebook.schemas

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class Media(
    @SerializedName("image") @Expose var image: NewsImage? = null,
    @SerializedName("source") @Expose var source: String? = null
)
