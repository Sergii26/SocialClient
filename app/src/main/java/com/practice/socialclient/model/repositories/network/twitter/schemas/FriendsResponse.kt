package com.practice.socialclient.model.repositories.network.twitter.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FriendsResponse(
    @SerializedName("users") @Expose var friends: Array<User>,
    @SerializedName("next_cursor_str") @Expose var nextCursorStr: String
)
