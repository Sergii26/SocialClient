package com.practice.socialclient.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.practice.socialclient.R
import com.practice.socialclient.databinding.NewsItemBinding
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.model.pojo.NewsInfo

class NewsListAdapter : RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {
    private val logger = Logger.withTag("MyLog")
    private val newsList: MutableList<NewsInfo> = ArrayList()

    fun getNewsList(): MutableList<NewsInfo> {
        return newsList
    }

    fun clearNewsList() {
        newsList.clear()
        this.notifyDataSetChanged()
    }

    fun addNewTweetsToList(newsList: MutableList<NewsInfo>) {
        this.newsList.addAll(newsList)
        this.notifyDataSetChanged()
        logger.log("news adapter addNewTweetsToList() list size: " + this.newsList.size)
    }

    fun setNewsList(newsList: MutableList<NewsInfo>) {
        logger.log("news adapter setNewsList(), list size: " + newsList.size)
        this.newsList.clear()
        this.newsList.addAll(newsList)
//        logger.log("news adapter setNewsList(), this.list size: " + this.newsList.size)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
//        logger.log("news adapter onCreateViewHolder()")
        val inflater = LayoutInflater.from(parent.context)
        val binding: NewsItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.news_item, parent, false)

        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
//        logger.log("news adapter onBindViewHolder(), position: $position")
        holder.bindView(newsList[position])
    }

    override fun getItemCount(): Int {
//        logger.log("news adapter getItemCount()" + newsList.size)
        return newsList.size
    }

    inner class NewsViewHolder(var binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(news: NewsInfo?) {
//            logger.log("news adapter bindView()" + news?.url)
            binding.newsInfo = news
            binding.executePendingBindings()
        }
    }
}