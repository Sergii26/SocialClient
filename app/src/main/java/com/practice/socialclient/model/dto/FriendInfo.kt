package com.practice.socialclient.model.dto

data class FriendInfo(var name: String, var pictureUrl: String, var source: String, var cursor: String) {

    companion object {
        const val SOURCE_FACEBOOK = "facebook"
        const val SOURCE_TWITTER = "twitter"
    }
}
