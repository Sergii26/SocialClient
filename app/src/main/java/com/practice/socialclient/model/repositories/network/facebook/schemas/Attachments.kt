package com.practice.socialclient.model.repositories.network.facebook.schemas

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class Attachments(
    @SerializedName("data") @Expose var attachmentsData: List<AttachmentsData?>? = null
)
