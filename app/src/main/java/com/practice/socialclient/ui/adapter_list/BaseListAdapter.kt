package com.practice.socialclient.ui.adapter_list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practice.socialclient.model.logger.Logger

abstract class BaseListAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemList = mutableListOf<T>()
    protected val logger = Logger.withTag("MyLog")

    fun getItemList(): List<T>{
        return itemList
    }

    fun setItemList(items: List<T>){
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    fun addNextPage(items: List<T>){
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    fun clearItemList(){
        itemList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Binder<T>).bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size


    abstract fun getViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder

    internal interface Binder<T> {
        fun bind(data: T)
    }
}