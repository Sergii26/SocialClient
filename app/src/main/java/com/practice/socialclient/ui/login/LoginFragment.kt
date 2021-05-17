package com.practice.socialclient.ui.login

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import com.facebook.login.widget.LoginButton
import com.practice.socialclient.R
import com.practice.socialclient.model.logger.ILog
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.model.twitter.TwitterConstants
import com.practice.socialclient.ui.arch.MvvmFragment


class LoginFragment : MvvmFragment<LoginContract.Host>() {
    private val logger: ILog = Logger.withTag("MyLog")
    private lateinit var btnTwitterLogin: ImageButton
    private lateinit var btnReady: Button
    private lateinit var btnFBLogin: LoginButton
    private lateinit var twitterDialog: Dialog

//    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoginContract.BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        DaggerLoginFragmentComponent.builder()
//            .loginFragmentModule(LoginFragmentModule())
//            .build()
//            .injectLoginFragment(this)
//        viewModel =
//            viewModelFactory.let { ViewModelProvider(this, it).get(LoginViewModel::class.java) }
        viewModel = ViewModelProvider(this, LoginViewModelFactory()).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnFBLogin = view.findViewById(R.id.login_button) as LoginButton
        btnTwitterLogin = view.findViewById(R.id.btnTwitterLogin)
        btnReady = view.findViewById(R.id.btnReady)
        btnFBLogin.setReadPermissions(viewModel.getFbPermissions())
        btnFBLogin.fragment = this
        btnTwitterLogin.setOnClickListener {
            viewModel.getRequestToken()
        }
        viewModel.twitterAuthUrl().observe(viewLifecycleOwner, { url: String ->
            logger.log("PhotosViewModel twitterAuthUrl()")
            if (url.isNotEmpty()) {
                setupTwitterWebViewDialog(url)
            }
        })
        viewModel.launchFB()

        viewModel.getLoginCheckingState().observe(viewLifecycleOwner, { result ->
            logger.log("PhotosViewModel getLoginCheckingState()")
            if (result == LoginViewModel.LOGIN_CHECKED) {
                if (hasCallBack()) {
                    callBack?.openNewsFragment()
                }
            }
        })

        btnReady.setOnClickListener {
            logger.log("PhotosViewModel setOnClickListener()")
            viewModel.checkLoginStates()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setupTwitterWebViewDialog(url: String) {
        logger.log("PhotosViewModel setupTwitterWebViewDialog()")
        twitterDialog = Dialog(requireContext())
        val webView = WebView(requireContext())
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = TwitterWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
        twitterDialog.setContentView(webView)
        twitterDialog.show()
    }

    @Suppress("OverridingDeprecatedMember")
    inner class TwitterWebViewClient : WebViewClient() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?, request: WebResourceRequest?
        ): Boolean {
            logger.log("PhotosViewModel shouldOverrideUrlLoading()")
            if (request?.url.toString().startsWith(TwitterConstants.CALLBACK_URL)) {
                logger.log("Authorization URL: " + request?.url.toString())
                viewModel.handleUrl(request?.url.toString())
                // Close the dialog after getting the oauth_verifier
                if (request?.url.toString().contains(TwitterConstants.CALLBACK_URL)) {
                    twitterDialog.dismiss()
                }
                return true
            }
            return false
        }

        // For API 19 and below
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(TwitterConstants.CALLBACK_URL)) {
                Log.d("Authorization URL: ", url)
                viewModel.handleUrl(url)
                // Close the dialog after getting the oauth_verifier
                if (url.contains(TwitterConstants.CALLBACK_URL)) {
                    twitterDialog.dismiss()
                }
                return true
            }
            return false
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.getCallBackManager().onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
