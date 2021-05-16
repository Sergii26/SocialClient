package com.practice.socialclient.model.pojo.facebook_pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class FriendInformation(
    @SerializedName("name") @Expose var name: String? = null,
    @SerializedName("id") @Expose var id: Long? = null,
    @SerializedName("picture") @Expose val picture: FriendPicture? = null
)











