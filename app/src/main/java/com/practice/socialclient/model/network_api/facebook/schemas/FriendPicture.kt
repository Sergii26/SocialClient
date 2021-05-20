package com.practice.socialclient.model.network_api.facebook.schemas

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class FriendPicture(
    @SerializedName("data") @Expose var data1te: Picture? = null
)
