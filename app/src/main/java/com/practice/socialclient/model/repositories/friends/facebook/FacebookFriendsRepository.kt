package com.practice.socialclient.model.repositories.friends.facebook

import com.facebook.AccessToken
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.repositories.network.facebook.FacebookNetworkClient
import com.practice.socialclient.model.repositories.friends.FriendsRepository
import com.practice.socialclient.model.dto.FriendInfo
import com.practice.socialclient.model.dto.FriendsCountInfo
import io.reactivex.Single

class FacebookFriendsRepository(
    private val facebookNetworkClient: FacebookNetworkClient,
    private val logger: Log
) : FriendsRepository {

    private var fbCursor: String = ""
    private val emptyString = ""
    private val zero = "0"

    override fun getFriendsCount(): Single<FriendsCountInfo> {
        logger.log("FacebookFriendsRepository getFriendsCount()")
        return facebookNetworkClient.getUserFriendsCount(getFbToken(), zero)

    }

    override fun getFriends(limit: String): Single<List<FriendInfo>> {
        logger.log("FacebookFriendsRepository getFriends()")
        return facebookNetworkClient.getUserFriends(getFbToken(), limit)
            .map { response ->
                if (response[0].cursor.isEmpty() || response[0].cursor == "null") {
                    fbCursor = ""
                } else {
                    fbCursor = response[0].cursor
                }
                response
            }
    }

    override fun getNextFriendsPage(limit: String): Single<List<FriendInfo>> {
        logger.log("FacebookFriendsRepository getFriendsNextPage()")
        if (fbCursor == "") return Single.just(ArrayList())

        return facebookNetworkClient.getNextFriendsPage(getFbToken(), fbCursor, limit)
            .map { response ->
                if (response[0].cursor.isEmpty() || response[0].cursor == "null") {
                    fbCursor = ""
                } else {
                    fbCursor = response[0].cursor
                }
                response
            }
    }

    override fun cleanCache() {
        fbCursor = emptyString
    }

    private fun getFbToken(): String {
        return AccessToken.getCurrentAccessToken().token
    }

}