package com.practice.socialclient.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.R
import com.practice.socialclient.model.logger.ILog
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.ui.arch.MvvmFragment

import javax.inject.Inject

class SplashFragment : MvvmFragment<SplashContract.Host?>() {
    private lateinit var viewModel: SplashContract.BaseSplashViewModel
    private val logger: ILog = Logger.withTag("MyLog")

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerSplashFragmentComponent.builder()
            .splashFragmentModule(SplashFragmentModule())
            .build()
            .injectSplashFragment(this)

        viewModel = viewModelFactory.let { ViewModelProvider(this, it).get(SplashViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getIsLoggedIn().observe(viewLifecycleOwner, Observer { isLoggedIn: Boolean ->
            logger.log("SplashFragment onReady() isReady= $isLoggedIn")
            if(hasCallBack()){
                if(isLoggedIn) {
                    callBack!!.jumpToNewsFragment()
                } else {
                    callBack!!.openLoginFragment()
                }
            }

        })
        viewModel.startTimer()
    }

}