package com.practice.socialclient.ui.news.item_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.navigation.NavigationView
import com.practice.socialclient.R
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.model.dto.NewsInfo
import com.practice.socialclient.ui.arch.FragmentIndication
import com.practice.socialclient.ui.arch.MvvmFragment
import com.practice.socialclient.ui.listener.EndlessScrollListener
import javax.inject.Inject

class ItemNewsFragment : MvvmFragment<ItemNewsContract.Host, ItemNewsContract.ViewModel>(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val logger: Log = Logger.withTag("MyLog")
    private lateinit var rvNews: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val adapter = ItemNewsListAdapter()
    private lateinit var fragmentType: String

    private val endlessScrollListener = object : EndlessScrollListener(false, true) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            logger.log("NewsFragment endListener")
            model!!.getNextNewsPage()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            model!!.onRefresh()
            model!!.getUserInfo()
            adapter.clearItemList()
            endlessScrollListener.resetState()
        }
        model!!.getTernOffRefreshingObservable().observe(viewLifecycleOwner, {
            logger.log("NewsFragment on obtain refreshingObservable")
            if (it) {
                swipeRefreshLayout.isRefreshing = false
            }
        })

        rvNews = view.findViewById(R.id.rvNews)
        rvNews.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvNews.adapter = adapter
        rvNews.addOnScrollListener(endlessScrollListener)
        model!!.getNewsListObservable().observe(viewLifecycleOwner, { list: MutableList<NewsInfo> ->
            logger.log("NewsFragment on obtain newsList")
            if (adapter.getItemList().isEmpty()) {
                adapter.setItemList(list)
            } else {
                adapter.addNextPage(list)
            }
            model!!.clearNewsList()
        })
        model!!.getInternetStateObservable().observe(viewLifecycleOwner, {
            logger.log("NewsFragment on obtain internetState")
            if (!it) {
                showToast(R.string.no_internet)
            }
        })

        model!!.getFbUserDataObservable().observe(viewLifecycleOwner, { userInfo ->
            callBack?.setFbUserData(userInfo)
        })

        model!!.getTwUserDataObservable().observe(viewLifecycleOwner, { userInfo ->
            callBack?.setTwUserData(userInfo)
        })

        if (savedInstanceState == null && hasCallBack()) {
        }

        callBack?.setNavListener(this)
    }

    override fun createModel(): ItemNewsContract.ViewModel {
        arguments?.takeIf { it.containsKey(FragmentIndication.KEY_INDICATION) }?.apply {
            viewModelFactory = DaggerItemNewsViewModelFactoryComponent.builder()
                .itemNewsViewModelFactoryModule(
                    ItemNewsViewModelFactoryModule(getString(
                        FragmentIndication.KEY_INDICATION).toString())
                )
                .build().provideItemNewsViewModelFactory()
            fragmentType = getString(FragmentIndication.KEY_INDICATION).toString()
        }
        return viewModelFactory.let {
            ViewModelProvider(
                this,
                it
            ).get(ItemNewsViewModel::class.java)
        }
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
