package com.practice.socialclient.model.repositories.friends.twitter

import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.network_api.twitter.TwitterNetworkClient
import com.practice.socialclient.model.network_api.twitter.schemas.User
import com.practice.socialclient.model.schemas.FriendInfo
import com.practice.socialclient.model.schemas.FriendsCountInfo
import com.practice.socialclient.model.repositories.friends.FriendsRepository
import io.reactivex.Single

class TwitterFriendsRepository(
    private val twitterNetworkClient: TwitterNetworkClient,
    private val logger: Log
) : FriendsRepository {

    private var twCursor: String = ""
    private val twitterSource = "Twitter friends: "

    override fun getFriendsCount(): Single<FriendsCountInfo> {
        logger.log("TwitterFriendsRepository getFriendsCount()")
        return twitterNetworkClient.getFriendsCount()
            .map { response ->
                response.friendsCount.toString()
                FriendsCountInfo(twitterSource, response.friendsCount.toString())
            }
    }

    override fun getFriends(limit: String): Single<List<FriendInfo>> {
        logger.log("TwitterFriendsRepository getFriends()")
        return twitterNetworkClient.getFriends(limit)
            .map { response ->
                twCursor = if (response.nextCursorStr == "0") {
                    ""
                } else {
                    response.nextCursorStr
                }
                val friendsList = ArrayList<FriendInfo>()
                friendsList.addAll(convertTwFriendsResponse(response.friends))
                friendsList
            }
    }

    override fun getNextFriendsPage(limit: String): Single<List<FriendInfo>> {
        logger.log("TwitterFriendsRepository getNextFriendsPage()")
        if (twCursor == "") {
            return Single.just(ArrayList())
        }
        return twitterNetworkClient.getNextFriendsPage(limit, twCursor)
            .map { response ->
                twCursor = if (response.nextCursorStr == "0") {
                    ""
                } else {
                    response.nextCursorStr
                }
                val friendsList = ArrayList<FriendInfo>()
                friendsList.addAll(convertTwFriendsResponse(response.friends))
                friendsList
            }
    }

    override fun cleanCache() {
        twCursor = ""
    }

    private fun convertTwFriendsResponse(response: Array<User>): MutableList<FriendInfo> {
        val convertedResponse: MutableList<FriendInfo> = ArrayList()
        response.forEach {
            convertedResponse.add(
                FriendInfo(
                    it.name.toString(),
                    it.profileImageUrlHttps.toString(),
                    FriendInfo.SOURCE_TWITTER
                )
            )
        }
        return convertedResponse
    }
}