package com.practice.socialclient.model.network_api.facebook.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserPhotosResponse(
    @SerializedName("data") @Expose var data: List<ImageData>? = null,
    @SerializedName("paging") @Expose var paging: Paging? = null
)
