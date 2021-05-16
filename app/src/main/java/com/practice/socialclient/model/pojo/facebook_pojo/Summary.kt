package com.practice.socialclient.model.pojo.facebook_pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Summary(
    @SerializedName("total_count") @Expose var totalCount: Int? = null
)