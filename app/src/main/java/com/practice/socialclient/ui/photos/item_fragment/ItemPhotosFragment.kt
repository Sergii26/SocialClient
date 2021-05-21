package com.practice.socialclient.ui.photos.item_fragment

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
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.model.dto.PhotoInfo
import com.practice.socialclient.ui.arch.FragmentIndication
import com.practice.socialclient.ui.arch.MvvmFragment
import com.practice.socialclient.ui.listener.EndlessScrollListener
import com.practice.socialclient.ui.news.item_fragment.ItemNewsContract
import javax.inject.Inject


class ItemPhotosFragment : MvvmFragment<ItemNewsContract.Host, ItemPhotosContract.ViewModel>(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var fragmentType: String
    private lateinit var rvPhotos: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val adapter = ItemPhotosListAdapter()
    private val logger = Logger.withTag("MyLog")

    private val scrollListener = object : EndlessScrollListener(false, true) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            model!!.getNextPhotosPage()
            model!!.getUserInfo()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPhotos = view.findViewById(R.id.rvPhotos)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            model!!.onRefresh()
            adapter.clearItemList()
            scrollListener.resetState()
        }
        rvPhotos.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvPhotos.adapter = adapter
        model!!.getPhotosObservable().observe(viewLifecycleOwner, { list: MutableList<PhotoInfo> ->
            logger.log("PhotosFragment fb onPhotosListChanged()")
            if (adapter.getItemList().isEmpty()){
                adapter.setItemList(list)
            } else {
                adapter.addNextPage(list)
            }
            model!!.clearPhotosList()
        })

        rvPhotos.addOnScrollListener(scrollListener)
        model!!.getInternetState().observe(viewLifecycleOwner, {
            if (!it) {
                showToast(R.string.no_internet)
            }
        })
        model!!.getTernOffRefreshing().observe(viewLifecycleOwner, {
            if (it) {
                swipeRefreshLayout.isRefreshing = false
            }
        })

        model!!.getFbUserDataObservable().observe(viewLifecycleOwner, { userInfo ->
            callBack?.setFbUserData(userInfo)
        })

        model!!.getTwUserDataObservable().observe(viewLifecycleOwner, { userInfo ->
            callBack?.setTwUserData(userInfo)
        })
        callBack?.setNavListener(this)
    }

    override fun createModel(): ItemPhotosContract.ViewModel {
        arguments?.takeIf { it.containsKey(FragmentIndication.KEY_INDICATION) }?.apply {
            viewModelFactory = DaggerItemPhotosViewModelFactoryComponent.builder()
                .itemPhotosViewModelFactoryModule(ItemPhotosViewModelFactoryModule(getString(FragmentIndication.KEY_INDICATION).toString()))
                .build().provideItemPhotosModelFactory()
            fragmentType = getString(FragmentIndication.KEY_INDICATION).toString()
        }
        return viewModelFactory.let { ViewModelProvider(this, it).get(ItemPhotosViewModel::class.java) }
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
