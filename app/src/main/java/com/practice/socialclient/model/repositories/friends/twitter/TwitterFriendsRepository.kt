package com.practice.socialclient.model.repositories.friends.twitter

import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.network_api.twitter.TwitterNetworkClient
import com.practice.socialclient.model.repositories.friends.FriendsRepository
import com.practice.socialclient.model.schemas.FriendInfo
import com.practice.socialclient.model.schemas.FriendsCountInfo
import io.reactivex.Single

class TwitterFriendsRepository(
    private val twitterNetworkClient: TwitterNetworkClient,
    private val logger: Log
) : FriendsRepository {

    private var twCursor: String = ""

    override fun getFriendsCount(): Single<FriendsCountInfo> {
        logger.log("TwitterFriendsRepository getFriendsCount()")
        return twitterNetworkClient.getFriendsCount()
    }

    override fun getFriends(limit: String): Single<List<FriendInfo>> {
        logger.log("TwitterFriendsRepository getFriends()")
        return twitterNetworkClient.getFriends(limit)
            .map { response ->
                twCursor = if (response[0].cursor == "0") {
                    ""
                } else {
                    response[0].cursor
                }
                response
            }
    }

    override fun getNextFriendsPage(limit: String): Single<List<FriendInfo>> {
        logger.log("TwitterFriendsRepository getNextFriendsPage()")
        if (twCursor == "") {
            return Single.just(ArrayList())
        }
        return twitterNetworkClient.getNextFriendsPage(limit, twCursor)
            .map { response ->
                twCursor = if (response[0].cursor == "0") {
                    ""
                } else {
                    response[0].cursor
                }
                response
            }
    }

    override fun cleanCache() {
        twCursor = ""
    }

}