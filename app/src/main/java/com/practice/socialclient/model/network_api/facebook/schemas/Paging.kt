package com.practice.socialclient.model.network_api.facebook.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Paging(
    @SerializedName("cursors") @Expose var cursors: Cursor? = null,
    @SerializedName("next") @Expose var next: String? = null
)
