package com.practice.socialclient.ui.adapter_binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.practice.socialclient.R
import com.practice.socialclient.model.schemas.FriendInfo

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageResource(imageView: ImageView, icon: String?) {
        when (icon) {
            FriendInfo.SOURCE_FACEBOOK -> Glide.with(imageView).load(R.drawable.icon_facebook_128px)
                .into(imageView)
            FriendInfo.SOURCE_TWITTER -> Glide.with(imageView).load(R.drawable.icon_twitter_128px)
                .into(imageView)
            else -> Glide.with(imageView).load(icon).into(imageView)
        }
    }
}
