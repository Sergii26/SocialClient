package com.practice.socialclient.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.practice.socialclient.R
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.ui.arch.MvvmFragment
import com.practice.socialclient.ui.login.LoginViewModel
import com.practice.socialclient.ui.login.LoginViewModelFactory

class NewsPagerFragment : Fragment() {

    private lateinit var newsPagerAdapter: NewsPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        newsPagerAdapter = NewsPagerAdapter(this, 2)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = newsPagerAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position == 0) {
                tab.text = "facebook news"
            }
            if (position == 1) {
                tab.text = "twitter news"
            }
        }.attach()

    }

}