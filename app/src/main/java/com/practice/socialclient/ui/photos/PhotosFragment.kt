package com.practice.socialclient.ui.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.practice.socialclient.R
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.ui.arch.MvvmFragment
import com.practice.socialclient.ui.listener.EndlessScrollListener
import com.practice.socialclient.ui.news.Contract


class PhotosFragment : MvvmFragment<Contract.Host>() {

//    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PhotosViewModel
    private lateinit var rvTwPhotos: RecyclerView
    private lateinit var rvFbPhotos: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val twPhotosAdapter = PhotosListAdapter()
    private val fbPhotosAdapter = PhotosListAdapter()
    private val logger = Logger.withTag("MyLog")
    private val twScrollListener = object : EndlessScrollListener(false, true) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            viewModel.downloadNextTwPage()
        }
    }
    private val fbScrollListener = object : EndlessScrollListener(false, true) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            viewModel.downloadNextFbPage()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        DaggerPhotosFragmentComponent
//            .builder()
//            .photosFragmentModule(PhotosFragmentModule())
//            .build()
//            .injectPhotosFragment(this)
//        viewModel =
//            viewModelFactory.let { ViewModelProvider(this, it).get(PhotosViewModel::class.java) }
        viewModel = ViewModelProvider(this, PhotosViewModelFactory()).get(PhotosViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkInternetConnection()
        rvTwPhotos = view.findViewById(R.id.rvTwPhotos)
        rvFbPhotos = view.findViewById(R.id.rvFbPhotos)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.onRefresh()
            fbPhotosAdapter.clearPhotosList()
            twPhotosAdapter.clearPhotosList()
            twScrollListener.resetState()
            fbScrollListener.resetState()
            if (hasCallBack()) callBack!!.downloadUserData()
        }
        val fbLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val twLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvTwPhotos.layoutManager = twLayoutManager
        rvFbPhotos.layoutManager = fbLayoutManager
        rvTwPhotos.adapter = twPhotosAdapter
        rvFbPhotos.adapter = fbPhotosAdapter
        viewModel.getFbUserPhotos().observe(viewLifecycleOwner, { list: MutableList<String> ->
            logger.log("PhotosFragment fb onPhotosListChanged()")
            if (fbPhotosAdapter.getPhotoList().size == 0) {
                fbPhotosAdapter.setPhotoList(list)
            } else {
                fbPhotosAdapter.addNewPhotosToList(list)
            }
            viewModel.clearFbPhotosList()
        })

        viewModel.getTwUserPhotos().observe(viewLifecycleOwner, { list: MutableList<String> ->
            logger.log("PhotosFragment tw onPhotosListChanged()")
            if (twPhotosAdapter.getPhotoList().size == 0) {
                twPhotosAdapter.setPhotoList(list)
            } else {
                twPhotosAdapter.addNewPhotosToList(list)
            }
            viewModel.clearTwPhotosList()
        })
        rvTwPhotos.addOnScrollListener(twScrollListener)
        rvFbPhotos.addOnScrollListener(fbScrollListener)
        viewModel.getInternetState().observe(viewLifecycleOwner, {
            if (!it) {
                showToast(R.string.no_internet)
            }
        })
        viewModel.getTernOffRefreshing().observe(viewLifecycleOwner, {
            if (it) {
                swipeRefreshLayout.isRefreshing = false
            }
        })
        viewModel.downloadPhotos()
    }
}
