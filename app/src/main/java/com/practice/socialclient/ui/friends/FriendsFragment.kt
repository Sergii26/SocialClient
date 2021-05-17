package com.practice.socialclient.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.practice.socialclient.R
import com.practice.socialclient.databinding.FragmentFriendsBinding
import com.practice.socialclient.model.logger.ILog
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.model.pojo.FriendInfo
import com.practice.socialclient.model.pojo.FriendsCountInfo
import com.practice.socialclient.ui.arch.MvvmFragment
import com.practice.socialclient.ui.listener.EndlessScrollListener
import javax.inject.Inject


class FriendsFragment : MvvmFragment<Contract.Host>() {

    private val logger: ILog = Logger.withTag("MyLog")

//    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: FriendsViewModel
    private lateinit var rvFbFriends: RecyclerView
    private lateinit var rvTwFriends: RecyclerView
    private lateinit var binding: FragmentFriendsBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val fbAdapter: FriendsListAdapter = FriendsListAdapter()
    private val twAdapter: FriendsListAdapter = FriendsListAdapter()
    private val twScrollListener = object : EndlessScrollListener(false, true) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            viewModel.getNextTwPage()
        }
    }
    private val fbScrollListener = object : EndlessScrollListener(false, true) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            viewModel.getNextFbPage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        DaggerFriendsFragmentComponent
//            .builder()
//            .friendsFragmentModule(FriendsFragmentModule())
//            .build()
//            .injectFriendsFragment(this)
//        viewModel = viewModelFactory.let { ViewModelProvider(this, it).get(FriendsViewModel::class.java) }
        viewModel = ViewModelProvider(this, NewFactory()).get(FriendsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkInternetConnection()
        rvFbFriends = view.findViewById(R.id.rvFbFriends)
        rvTwFriends = view.findViewById(R.id.rvTwFriends)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.onRefresh()
            fbAdapter.clearFriendsList()
            twAdapter.clearFriendsList()
            twScrollListener.resetState()
            fbScrollListener.resetState()
            if (hasCallBack()) callBack!!.downloadUserData()
        }
        rvFbFriends.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvFbFriends.adapter = fbAdapter

        viewModel.getFbFriendsList().observe(viewLifecycleOwner, { friendsList: MutableList<FriendInfo> ->
            logger.log("FriendsFragment getFbFriendsList()")
            if(fbAdapter.getFriendsList().size == 0){
                fbAdapter.setFriendsList(friendsList)
            } else {
                fbAdapter.addNextPage(friendsList)
            }
            viewModel.clearFbFriendsList()
        })

        rvTwFriends.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvTwFriends.adapter = twAdapter

        viewModel.getTwFriendsList().observe(viewLifecycleOwner, { friendsList: MutableList<FriendInfo> ->
            logger.log("FriendsFragment getTwFriendsList()")
            logger.log("FriendsFragment getTwFriendsList works")
            if(twAdapter.getFriendsList().size == 0){
                twAdapter.setFriendsList(friendsList)
            } else {
                twAdapter.addNextPage(friendsList)
            }
            viewModel.clearTwFriendsList()
        })
        rvTwFriends.addOnScrollListener(twScrollListener)
        rvFbFriends.addOnScrollListener(fbScrollListener)
        viewModel.getFriendsCount().observe(viewLifecycleOwner, { friendsCountInfo: FriendsCountInfo ->
            logger.log("FriendsFragment getFriendsCount()")
            binding.friendsCount = friendsCountInfo
        })

        viewModel.getInternetState().observe(viewLifecycleOwner, {
            logger.log("FriendsFragment getInternetState()")
            if (!it) showToast(R.string.no_internet)
        })

        viewModel.getTernOffRefreshing().observe(viewLifecycleOwner, {
            if (it) swipeRefreshLayout.isRefreshing = false
        })

        viewModel.getFriends()
        viewModel.getFriendsTotalCount()
    }
}