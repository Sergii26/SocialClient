package com.practice.socialclient.model.pojo.facebook_pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Reactions(
    @SerializedName("summary") @Expose var summary: Summary
)