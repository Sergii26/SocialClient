package com.practice.socialclient.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.practice.socialclient.R

class FriendsPagerFragment : Fragment() {

    private lateinit var friendsPagerAdapter: FriendsPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        friendsPagerAdapter = FriendsPagerAdapter(this, 2)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = friendsPagerAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if(position == 0){
                tab.text = "facebook friends"
            }
            if(position==1){
                tab.text = "twitter friends"
            }
        }.attach()
    }

}
