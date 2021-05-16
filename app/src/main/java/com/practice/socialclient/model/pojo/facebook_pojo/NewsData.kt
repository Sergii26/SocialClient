package com.practice.socialclient.model.pojo.facebook_pojo

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class NewsData(
    @SerializedName("type") @Expose var type: String? = null,
    @SerializedName("created_time") @Expose var createdTime: String? = null,
    @SerializedName("message") @Expose var message: String? = null,
    @SerializedName("id") @Expose var id: String? = null,
    @SerializedName("attachments") @Expose var attachments: Attachments? = null,
    @SerializedName("reactions") @Expose var reactions: Reactions? = null,
    @SerializedName("comments") @Expose var comments: Comments? = null
)
