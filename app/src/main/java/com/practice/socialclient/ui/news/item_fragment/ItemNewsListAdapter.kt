package com.practice.socialclient.ui.news.item_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.practice.socialclient.R
import com.practice.socialclient.databinding.ItemNewsBinding
import com.practice.socialclient.model.schemas.NewsInfo
import com.practice.socialclient.ui.adapter_list.BaseListAdapter

class ItemNewsListAdapter : BaseListAdapter<NewsInfo>() {

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemNewsBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_news, parent, false)

        return NewsViewHolder(binding)
    }

    inner class NewsViewHolder(var binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<NewsInfo> {
        override fun bind(data: NewsInfo) {
            binding.newsInfo = data
            binding.executePendingBindings()
        }
    }
}