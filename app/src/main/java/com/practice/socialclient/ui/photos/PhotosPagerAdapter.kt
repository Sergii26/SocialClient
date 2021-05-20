package com.practice.socialclient.ui.photos

import androidx.fragment.app.Fragment
import com.practice.socialclient.ui.adapter_pager.BasePagerAdapter
import com.practice.socialclient.ui.photos.item_fragment.ItemPhotosFragment

class PhotosPagerAdapter(fragment: Fragment, pagesCount: Int) : BasePagerAdapter(fragment, pagesCount) {
    override fun initFragment(): Fragment {
        return ItemPhotosFragment()
    }
}