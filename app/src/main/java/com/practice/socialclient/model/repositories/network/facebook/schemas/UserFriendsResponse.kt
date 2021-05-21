package com.practice.socialclient.model.repositories.network.facebook.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserFriendsResponse(
    @SerializedName("data") @Expose var data: List<FriendInformation>? = null,
    @SerializedName("summary") @Expose var summary: Summary? = null,
    @SerializedName("paging") @Expose var paging: Paging? = null
)
