package com.practice.socialclient.model.pojo.facebook_pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class UserFriendsResponse(
    @SerializedName("data") @Expose var data: List<FriendInformation>? = null,
    @SerializedName("summary") @Expose var summary: Summary? = null,
    @SerializedName("paging") @Expose var paging: Paging? = null
)






