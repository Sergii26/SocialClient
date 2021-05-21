package com.practice.socialclient.model.repositories.network.facebook.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PictureData(@SerializedName("data") @Expose var picture: Picture? = null)
