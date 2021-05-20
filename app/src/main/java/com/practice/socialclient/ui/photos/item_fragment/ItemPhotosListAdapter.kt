package com.practice.socialclient.ui.photos.item_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.practice.socialclient.R
import com.practice.socialclient.databinding.ItemPhotoBinding
import com.practice.socialclient.ui.adapter_list.BaseListAdapter

class ItemPhotosListAdapter : BaseListAdapter<String>() {

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemPhotoBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_photo, parent, false)
        return PhotoViewHolder(binding)
    }

    inner class PhotoViewHolder(var binding: ItemPhotoBinding): RecyclerView.ViewHolder(binding.root), Binder<String>{
         override fun bind(data: String) {
             binding.photoUrl = data
             binding.executePendingBindings()
         }
     }

}