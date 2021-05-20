package com.practice.socialclient.ui.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.practice.socialclient.R
import com.practice.socialclient.ui.news.NewsPagerAdapter

class PhotosPagerFragment: Fragment() {

    private lateinit var photosPagerAdapter: PhotosPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        photosPagerAdapter = PhotosPagerAdapter(this, 2)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = photosPagerAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if(position == 0){
                tab.text = "facebook photos"
            }
            if(position==1){
                tab.text = "twitter photos"
            }
        }.attach()
    }

}