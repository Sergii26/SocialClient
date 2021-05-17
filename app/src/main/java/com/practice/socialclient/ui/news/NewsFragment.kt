package com.practice.socialclient.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.practice.socialclient.R
import com.practice.socialclient.model.logger.ILog
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.model.pojo.NewsInfo
import com.practice.socialclient.ui.arch.MvvmFragment
import com.practice.socialclient.ui.listener.EndlessScrollListener
import com.practice.socialclient.ui.login.LoginViewModel
import com.practice.socialclient.ui.login.NewLoginFactory
import javax.inject.Inject


class NewsFragment : MvvmFragment<Contract.Host>() {
    private val logger: ILog = Logger.withTag("MyLog")
//    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: NewsViewModel
    private lateinit var rvNews: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val adapter = NewsListAdapter()

    private val endlessScrollListener = object : EndlessScrollListener(false, true) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            logger.log("NewsFragment endListener")
            viewModel.getNextPage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        DaggerNewsFragmentComponent
//            .builder()
//            .newsFragmentModule(NewsFragmentModule())
//            .build()
//            .injectNewsFragment(this)
//        viewModel =
//            viewModelFactory.let { ViewModelProvider(this, it).get(NewsViewModel::class.java) }
        viewModel = ViewModelProvider(this, NewNewsFactory()).get(NewsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkInternetConnection()
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.onRefresh()
            adapter.clearNewsList()
            endlessScrollListener.resetState()
            if (hasCallBack()) callBack!!.downloadUserData()
        }
        viewModel.getTernOffRefreshing().observe(viewLifecycleOwner, {
            logger.log("NewsFragment getTernOffRefreshing")
            if (it) swipeRefreshLayout.isRefreshing = false
        })
        rvNews = view.findViewById(R.id.rvNews)
        rvNews.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvNews.adapter = adapter
        rvNews.addOnScrollListener(endlessScrollListener)
        viewModel.getNewsList().observe(viewLifecycleOwner, { list: MutableList<NewsInfo> ->
            logger.log("NewsFragment getNewsList")
            if (adapter.getNewsList().size != 0) {
                adapter.addNewTweetsToList(list)
            } else {
                adapter.setNewsList(list)
            }
            viewModel.clearNewsList()
        })
        viewModel.getInternetState().observe(viewLifecycleOwner, {
            logger.log("NewsFragment getInternetState")
            if (!it) showToast(R.string.no_internet)
        })

        if (savedInstanceState == null && hasCallBack()) {
            callBack!!.downloadUserData()
        }
        viewModel.downloadNews()
    }

}