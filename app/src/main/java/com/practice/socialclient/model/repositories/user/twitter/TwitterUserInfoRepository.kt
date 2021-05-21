package com.practice.socialclient.model.repositories.user.twitter

import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.repositories.network.twitter.TwitterNetworkClient
import com.practice.socialclient.model.dto.UserInfo
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