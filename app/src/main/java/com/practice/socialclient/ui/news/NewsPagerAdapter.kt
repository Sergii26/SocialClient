package com.practice.socialclient.ui.news

import androidx.fragment.app.Fragment
import com.practice.socialclient.ui.adapter_pager.BasePagerAdapter
import com.practice.socialclient.ui.news.item_fragment.ItemNewsFragment

class NewsPagerAdapter(fragment: Fragment, pagesCount: Int) : BasePagerAdapter(fragment, pagesCount) {
    override fun initFragment(): Fragment {
        return ItemNewsFragment()
    }
}

