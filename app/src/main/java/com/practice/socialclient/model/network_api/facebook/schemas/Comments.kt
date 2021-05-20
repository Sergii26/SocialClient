package com.practice.socialclient.model.network_api.facebook.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Comments(
    @SerializedName("summary") @Expose var summary: Summary
)
