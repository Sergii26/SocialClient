package com.practice.socialclient.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.practice.socialclient.R
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.ui.arch.MvvmFragment

class SplashFragment : MvvmFragment<SplashContract.Host?, SplashContract.ViewModel>() {

    private val logger: Log = Logger.withTag("MyLog")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model!!.getIsLoggedInObservable().observe(viewLifecycleOwner, { isLoggedIn: Boolean ->
            logger.log("SplashFragment onReady() isReady= $isLoggedIn")
            if (hasCallBack()) {
                callBack!!.jumpToNewsFragment()
            }
        })

        model!!.getIsNotLoggedInObservable().observe(viewLifecycleOwner, { isLoggedIn: Boolean ->
            logger.log("SplashFragment onReady() isReady= $isLoggedIn")
            if (hasCallBack()) {
                    callBack!!.openLoginFragment()
                }
        })
    }

    override fun createModel(): SplashContract.ViewModel {
        return ViewModelProvider(this, SplashViewModelFactory()).get(SplashViewModel::class.java)
    }
}
