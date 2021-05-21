package com.practice.socialclient.model.schemas

data class NewsInfo(
    var id: String,
    var text: String,
    var url: String,
    var sourceIconUrl: String,
    var sourceName: String,
    var commentsOrRetweetsCount: String,
    var likeCount: String,
    var createdAtUnix: Long,
    var source: String,
    var createdAt: String,
    var cursor: String
) {

    constructor() : this("", "", "", "", "", "", "", 0, "", "", "")

    companion object {
        const val SOURCE_FACEBOOK = "facebook"
        const val SOURCE_TWITTER = "twitter"
    }
}
