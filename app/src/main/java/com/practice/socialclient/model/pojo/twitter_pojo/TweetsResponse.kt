package com.practice.socialclient.model.pojo.twitter_pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TweetsResponse(
    @SerializedName("text") @Expose var text: String?,
    @SerializedName("full_text") @Expose var fullText: String?,
    @SerializedName("created_at") @Expose var createdAt: String?,
    @SerializedName("entities") @Expose var entities: Entities?,
    @SerializedName("extended_entities") @Expose var extendedEntities: Entities?,
    @SerializedName("user") @Expose var user: User?,
    @SerializedName("favorite_count") @Expose var likeCount: Int?,
    @SerializedName("retweet_count") @Expose var retweetCount: Int?,
    @SerializedName("id") @Expose var id: Long?
)
