package com.practice.socialclient.model.repositories.user.twitter

import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.network_api.twitter.TwitterNetworkClient
import com.practice.socialclient.model.schemas.UserInfo
import com.practice.socialclient.model.repositories.user.UserInfoRepository
import io.reactivex.Single

class TwitterUserInfoRepository(
    private val twitterNetworkClient: TwitterNetworkClient,
    private val logger: Log
) : UserInfoRepository {

    override fun getUserInfo(): Single<UserInfo> {
        logger.log("TwitterUserInfoRepository getUserInfo()")
        return twitterNetworkClient.getUserData()

    }
}