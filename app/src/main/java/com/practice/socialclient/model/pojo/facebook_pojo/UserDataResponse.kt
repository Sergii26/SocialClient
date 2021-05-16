package com.practice.socialclient.model.pojo.facebook_pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserDataResponse(
    @SerializedName("name") @Expose var name: String? = null,
    @SerializedName("picture") @Expose var pictureData: PictureData? = null
)
