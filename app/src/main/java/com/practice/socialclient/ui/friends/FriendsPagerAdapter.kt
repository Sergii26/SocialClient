package com.practice.socialclient.ui.friends

import androidx.fragment.app.Fragment
import com.practice.socialclient.ui.adapter_pager.BasePagerAdapter
import com.practice.socialclient.ui.friends.item_fragment.ItemFriendsFragment

class FriendsPagerAdapter(fragment: Fragment, pagesCount: Int) : BasePagerAdapter(fragment, pagesCount) {
    override fun initFragment(): Fragment {
        return ItemFriendsFragment()
    }
}

