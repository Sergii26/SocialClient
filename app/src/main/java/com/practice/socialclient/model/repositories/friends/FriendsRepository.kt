package com.practice.socialclient.model.repositories.friends

import com.practice.socialclient.model.dto.FriendInfo
import com.practice.socialclient.model.dto.FriendsCountInfo
import io.reactivex.Single

interface FriendsRepository {
    fun getFriendsCount(): Single<FriendsCountInfo>
    fun getFriends(limit: String): Single<List<FriendInfo>>
    fun getNextFriendsPage(limit: String): Single<List<FriendInfo>>
    fun cleanCache()
}