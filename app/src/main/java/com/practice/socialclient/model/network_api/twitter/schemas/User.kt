package com.practice.socialclient.model.network_api.twitter.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("profile_image_url_https") @Expose var profileImageUrlHttps: String?,
    @SerializedName("name") @Expose var name: String?
)
