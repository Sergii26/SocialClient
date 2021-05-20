package com.practice.socialclient.model.repositories.friends.facebook

import com.facebook.AccessToken
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.network_api.facebook.FacebookNetworkClient
import com.practice.socialclient.model.network_api.facebook.schemas.UserFriendsResponse
import com.practice.socialclient.model.schemas.FriendInfo
import com.practice.socialclient.model.schemas.FriendsCountInfo
import com.practice.socialclient.model.repositories.friends.FriendsRepository
import io.reactivex.Single

class FacebookFriendsRepository(private val facebookNetworkClient: FacebookNetworkClient,
                                private val logger: Log) : FriendsRepository {

    private var fbCursor: String = ""
    private val emptyString = ""
    private val zero = "0"
    private val facebookSource = "Facebook friends: "

    override fun getFriendsCount(): Single<FriendsCountInfo> {
        logger.log("FacebookFriendsRepository getFriendsCount()")
        return facebookNetworkClient.getUserFriends(getFbToken(), zero)
            .map { response -> FriendsCountInfo(facebookSource, response.summary?.totalCount?.toString() ?: zero) }
    }

    override fun getFriends(limit: String): Single<List<FriendInfo>> {
        logger.log("FacebookFriendsRepository getFriends()")
        return facebookNetworkClient.getUserFriends(getFbToken(), limit)
            .map { response ->
                fbCursor = response.paging?.cursors?.after ?: emptyString
                val friendsList = ArrayList<FriendInfo>()
                if (response.data?.size != 0) {
                    friendsList.addAll(convertFbFriendsResponse(response))
                }
                friendsList
            }
    }

    override fun getNextFriendsPage(limit: String): Single<List<FriendInfo>> {
        logger.log("FacebookFriendsRepository getFriendsNextPage()")
        if (fbCursor == "") return Single.just(ArrayList())

        return facebookNetworkClient.getNextFriendsPage(getFbToken(), fbCursor, limit)
            .map { response ->
                fbCursor = response.paging?.cursors?.after ?: emptyString
                val friendsList = ArrayList<FriendInfo>()
                if (response.data?.size != 0) friendsList.addAll(convertFbFriendsResponse(response))
                friendsList
            }
    }

    override fun cleanCache() {
        fbCursor = emptyString
    }

    private fun getFbToken(): String {
        return AccessToken.getCurrentAccessToken().token
    }

    private fun convertFbFriendsResponse(response: UserFriendsResponse): MutableList<FriendInfo> {
        val dataResponse = response.data
        val convertedResponse: MutableList<FriendInfo> = ArrayList()
        dataResponse?.forEach {
            convertedResponse.add(
                FriendInfo(
                    it.name.toString(),
                    it.picture?.data1te?.url.toString(),
                    FriendInfo.SOURCE_FACEBOOK
                )
            )
        }
        return convertedResponse
    }
}