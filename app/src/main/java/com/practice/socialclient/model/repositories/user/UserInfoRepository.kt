package com.practice.socialclient.model.repositories.user

import com.practice.socialclient.model.dto.UserInfo
import io.reactivex.Single

interface UserInfoRepository {
    fun getUserInfo(): Single<UserInfo>
}