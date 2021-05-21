package com.practice.socialclient.model.repositories.network.facebook.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserDataResponse(
    @SerializedName("name") @Expose var name: String? = null,
    @SerializedName("picture") @Expose var pictureData: PictureData? = null
)
