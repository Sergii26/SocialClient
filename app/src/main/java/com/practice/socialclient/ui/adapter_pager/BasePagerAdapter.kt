package com.practice.socialclient.ui.adapter_pager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practice.socialclient.ui.arch.FragmentIndication

abstract class BasePagerAdapter (fragment: Fragment, private val pagesCount: Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = pagesCount

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = initFragment()
        if(position == 0){
            fragment.arguments = Bundle().apply {
                putString(FragmentIndication.KEY_INDICATION, FragmentIndication.FACEBOOK_INDICATION)
            }
        } else {
            fragment.arguments = Bundle().apply {
                putString(FragmentIndication.KEY_INDICATION, FragmentIndication.TWITTER_INDICATION)
            }
        }

        return fragment
    }

    abstract fun initFragment(): Fragment

}