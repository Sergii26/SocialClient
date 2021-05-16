package com.practice.socialclient.ui.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.practice.socialclient.R
import com.practice.socialclient.databinding.PhotoItemBinding
import com.practice.socialclient.model.logger.Logger

class PhotosListAdapter : RecyclerView.Adapter<PhotosListAdapter.PhotoViewHolder>() {

    private val photoList: MutableList<String> = ArrayList()
    private val logger = Logger.withTag("MyLog")

    fun getPhotoList():MutableList<String> {
        return photoList
    }

    fun setPhotoList(photoList: MutableList<String>){
        this.photoList.clear()
        this.photoList.addAll(photoList)
        this.notifyDataSetChanged()
    }

    fun addNewPhotosToList(photoList: MutableList<String>){
        this.photoList.addAll(photoList)
        logger.log("PhotoAdapter addNewPhotosToList(), this.size : " + this.photoList.size)
        this.notifyDataSetChanged()
    }

    fun clearPhotosList(){
        this.photoList.clear()
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: PhotoItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.photo_item, parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bindView(photoList[position])
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    inner class PhotoViewHolder(var binding: PhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(url: String?) {
            binding.photoUrl = url
            binding.executePendingBindings()
        }
    }
}