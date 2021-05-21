package com.practice.socialclient.ui.friends.item_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.navigation.NavigationView
import com.practice.socialclient.R
import com.practice.socialclient.databinding.FragmentFriendsBinding
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.model.dto.FriendInfo
import com.practice.socialclient.model.dto.FriendsCountInfo
import com.practice.socialclient.ui.arch.FragmentIndication
import com.practice.socialclient.ui.arch.MvvmFragment
import com.practice.socialclient.ui.listener.EndlessScrollListener
import javax.inject.Inject

class ItemFriendsFragment : MvvmFragment<ItemFriendsContract.Host, ItemFriendsContract.ViewModel>(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val logger: Log = Logger.withTag("MyLog")
    private lateinit var fragmentType : String
    private lateinit var rvFriends: RecyclerView
    private lateinit var binding: FragmentFriendsBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val adapter: ItemFriendsListAdapter = ItemFriendsListAdapter()


    private val fbScrollListener = object : EndlessScrollListener(false, true) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            model!!.getNextFriendsPage()
            model!!.getUserInfo()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvFriends = view.findViewById(R.id.rvFriends)
        rvFriends.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvFriends.adapter = adapter
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            model!!.onRefresh()
            adapter.clearItemList()
            fbScrollListener.resetState()
        }

        rvFriends.addOnScrollListener(fbScrollListener)
        model!!.getFriendsCount()
            .observe(viewLifecycleOwner, { friendsCountInfo: FriendsCountInfo ->
                logger.log("ItemFriendsFragment friendsCountInfo()")
                binding.friendsCount = friendsCountInfo
            })

        model!!.getInternetState().observe(viewLifecycleOwner, {
            logger.log("ItemFriendsFragment getInternetState()")
            if (!it) {
                showToast(R.string.no_internet)
            }
        })

        model!!.getTernOffRefreshing().observe(viewLifecycleOwner, {
            logger.log("ItemFriendsFragment getTernOffRefreshing()")
            if (it) {
                swipeRefreshLayout.isRefreshing = false
            }
        })

        model!!.getFriendsListObservable()
            .observe(viewLifecycleOwner, { friendsList: List<FriendInfo> ->
                logger.log("ItemFriendsFragment getFbFriendsList() list size: ${friendsList.size}")
                if (adapter.getItemList().isEmpty()) {
                    adapter.setItemList(friendsList)
                } else {
                    adapter.addNextPage(friendsList)
                }
                model!!.clearFriendsList()
            })

        model!!.getFbUserDataObservable().observe(viewLifecycleOwner, { userInfo ->
            callBack?.setFbUserData(userInfo)
        })

        model!!.getTwUserDataObservable().observe(viewLifecycleOwner, { userInfo ->
            callBack?.setTwUserData(userInfo)
        })

        callBack?.setNavListener(this)
    }

    override fun createModel(): ItemFriendsContract.ViewModel {
        arguments?.takeIf { it.containsKey(FragmentIndication.KEY_INDICATION) }?.apply {
            viewModelFactory = DaggerItemFriendsViewModelComponent.builder()
                .itemFriendsViewModelModule(ItemFriendsViewModelModule(getString(FragmentIndication.KEY_INDICATION).toString()))
                .build().provideItemFriendsModelFactory()
            fragmentType = getString(FragmentIndication.KEY_INDICATION).toString()
        }
        return viewModelFactory.let { ViewModelProvider(this, it).get(ItemFriendsViewModel::class.java) }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        logger.log("ItemNewsFragment onNavigationItemSelected item id: $item")
        when (item.itemId) {

            R.id.friendsFragment -> {
                callBack?.let {
                    it.moveToFriendsFragment()
                    it.closeDrawer()
                }

            }

            R.id.photosFragment -> {
                callBack?.let {
                    it.moveToPhotosFragment()
                    it.closeDrawer()
                }
            }

            R.id.newsFragment -> {
                callBack?.let {
                    it.moveToNewsFragment()
                    it.closeDrawer()
                }
            }

            R.id.twLogin, R.id.fbLogin -> {
                callBack?.let {
                    it.moveToLoginFragment()
                    it.hideNavigationMenu()
                    it.closeDrawer()
                }
            }
            R.id.logout -> {
                model!!.logOut()
                callBack?.let {
                    it.moveToLoginFragment()
                    it.hideNavigationMenu()
                    it.closeDrawer()
                }
            }

        }
        return true
    }
}
