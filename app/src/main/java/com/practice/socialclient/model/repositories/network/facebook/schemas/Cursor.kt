package com.practice.socialclient.model.repositories.network.facebook.schemas

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class Cursor(
    @SerializedName("before") @Expose var before: String? = null,
    @SerializedName("after") @Expose var after: String? = null
)
