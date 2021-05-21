package com.practice.socialclient.model.repositories.network.facebook.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReactionsData(
    @SerializedName("id") @Expose var id: String,
    @SerializedName("name") @Expose var name: String,
    @SerializedName("type") @Expose var type: String
)
