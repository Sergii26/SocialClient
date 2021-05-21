package com.practice.socialclient.ui.friends.item_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.practice.socialclient.R
import com.practice.socialclient.databinding.ItemFriendBinding
import com.practice.socialclient.model.dto.FriendInfo
import com.practice.socialclient.ui.adapter_list.BaseListAdapter

class ItemFriendsListAdapter : BaseListAdapter<FriendInfo>() {

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding: ItemFriendBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_friend, parent, false)
        return FriendViewHolder(binding)
    }

   inner class FriendViewHolder(var binding: ItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<FriendInfo> {
        override fun bind(data: FriendInfo) {
            binding.friend = data
            binding.executePendingBindings()
        }
    }
}