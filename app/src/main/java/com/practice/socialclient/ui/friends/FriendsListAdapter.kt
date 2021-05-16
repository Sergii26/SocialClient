package com.practice.socialclient.ui.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.practice.socialclient.R
import com.practice.socialclient.databinding.FriendItemBinding
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.model.pojo.FriendInfo

class FriendsListAdapter : RecyclerView.Adapter<FriendsListAdapter.FriendViewHolder>() {

    private val friendsList: MutableList<FriendInfo> = ArrayList()
    private val logger = Logger.withTag("MyLog")

    fun getFriendsList(): MutableList<FriendInfo> {
        return friendsList
    }

    fun clearFriendsList() {
        friendsList.clear()
        notifyDataSetChanged()
    }

    fun setFriendsList(friendsList: MutableList<FriendInfo>) {
        this.friendsList.clear()
        this.friendsList.addAll(friendsList)
        this.notifyDataSetChanged()
    }

    fun addNextPage(friendsList: MutableList<FriendInfo>) {
        this.friendsList.addAll(friendsList)
        this.notifyDataSetChanged()
        logger.log("FriendsAdapter list size: ${this.friendsList.size}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: FriendItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.friend_item, parent, false)
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bindView(friendsList[position])
    }

    override fun getItemCount(): Int {
        return friendsList.size
    }

    inner class FriendViewHolder(var binding: FriendItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(friendInfo: FriendInfo?) {
            binding.friend = friendInfo
            binding.executePendingBindings()
        }
    }
}