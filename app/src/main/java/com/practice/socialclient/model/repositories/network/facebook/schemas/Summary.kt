package com.practice.socialclient.model.repositories.network.facebook.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Summary(
    @SerializedName("total_count") @Expose var totalCount: Int? = null
)
