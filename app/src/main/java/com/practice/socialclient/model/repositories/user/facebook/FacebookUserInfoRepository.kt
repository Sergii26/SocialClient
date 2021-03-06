package com.practice.socialclient.model.repositories.user.facebook

import com.facebook.AccessToken
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.repositories.network.facebook.FacebookNetworkClient
import com.practice.socialclient.model.dto.UserInfo
import com.practice.socialclient.model.repositories.user.UserInfoRepository
import io.reactivex.Single

class FacebookUserInfoRepository(
    private val facebookNetworkClient: FacebookNetworkClient,
    private val logger: Log
) : UserInfoRepository {

    override fun getUserInfo(): Single<UserInfo> {
        logger.log("FacebookUserInfoRepository getUserInfo()")
        return facebookNetworkClient.getUserData(getAccToken())
    }

    private fun getAccToken(): String {
        return AccessToken.getCurrentAccessToken().token
    }
}