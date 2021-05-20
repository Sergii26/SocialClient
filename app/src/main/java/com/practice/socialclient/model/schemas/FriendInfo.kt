package com.practice.socialclient.model.schemas

data class FriendInfo(var name: String, var pictureUrl: String, var source: String) {

    companion object {
        const val SOURCE_FACEBOOK = "facebook"
        const val SOURCE_TWITTER = "twitter"
    }
}
