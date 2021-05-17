package com.practice.socialclient.ui

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.practice.socialclient.R
import com.practice.socialclient.databinding.NavHeaderBinding
import com.practice.socialclient.model.logger.ILog
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.model.pojo.UserInfo
import com.practice.socialclient.ui.arch.Contract
import com.practice.socialclient.ui.login.LoginContract
import com.practice.socialclient.ui.splash.SplashContract

class MainActivity : AppCompatActivity(), Contract.Host, SplashContract.Host, LoginContract.Host,
    com.practice.socialclient.ui.news.Contract.Host,
    com.practice.socialclient.ui.friends.Contract.Host,
    com.practice.socialclient.ui.photos.Contract.Host,
    NavigationView.OnNavigationItemSelectedListener {
    private val logger: ILog = Logger.withTag("MyLog")
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navigationView: NavigationView
    private var actionBar: ActionBar? = null

//    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainActivityContract.BaseMainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        DaggerMainActivityComponent
//            .builder()
//            .mainActivityModule(MainActivityModule())
//            .build()
//            .injectContentActivity(this)

        navController = findNavController(R.id.nav_host_fragment)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_content_view)
        navigationView.setupWithNavController(navController)
        val headerBinding = NavHeaderBinding.inflate(layoutInflater)
        navigationView.addHeaderView(headerBinding.root)
        navigationView.setNavigationItemSelectedListener(this)
        val topLevelDestinations: MutableSet<Int> = HashSet()
        topLevelDestinations.add(R.id.newsFragment)
        topLevelDestinations.add(R.id.friendsFragment)
        topLevelDestinations.add(R.id.photosFragment)
        appBarConfiguration = AppBarConfiguration(topLevelDestinations, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        actionBar = supportActionBar
        val navMenu = navigationView.menu


//        viewModel = viewModelFactory.let { ViewModelProvider(this, it).get(MainActivityViewModel::class.java)}
        viewModel = ViewModelProvider(this, MainActivityViewModelFactory()).get(MainActivityViewModel::class.java)

//        viewModel = viewModelFactory.let {
//            ViewModelProvider(
//                this,
//                it
//            ).get(MainActivityViewModel::class.java)
//        }


        viewModel.checkInternetConnection()
        viewModel.getFbUserData().observe(this, { it ->
            logger.log("MainActivity observe FB USer - name: ${it.name}, iconUrl: ${it.iconUrl}")
            if (it.name.isEmpty() && it.iconUrl.isEmpty()) {
                findViewById<CardView>(R.id.cvFbUserPhoto)?.let { it.visibility = View.INVISIBLE }
                navMenu.findItem(R.id.fbLogin).isVisible = true
                headerBinding.fbUserData = UserInfo(getString(R.string.login_to_facebook), "")
            } else {
                findViewById<CardView>(R.id.cvFbUserPhoto)?.let { it.visibility = View.VISIBLE }
                navMenu.findItem(R.id.fbLogin).isVisible = false
                headerBinding.fbUserData = it
            }
        })

        viewModel.getTwUserData().observe(this, { it ->
            logger.log("MainActivity observe TW USer - name: ${it.name}, iconUrl: ${it.iconUrl}")
            if (it.name.isEmpty() && it.iconUrl.isEmpty()) {
                findViewById<CardView>(R.id.cvTwUserPhoto)?.let { it.visibility = View.INVISIBLE }
                navMenu.findItem(R.id.twLogin).isVisible = true
                headerBinding.twUserData = UserInfo(getString(R.string.login_to_twitter), "")
            } else {
                findViewById<CardView>(R.id.cvTwUserPhoto)?.let { it.visibility = View.VISIBLE }
                navMenu.findItem(R.id.twLogin).isVisible = false
                headerBinding.twUserData = it
            }
        })

        viewModel.getInternetState().observe(this, {
            if (!it) Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show()
        })

        if (savedInstanceState == null || navController.currentDestination?.id == R.id.splashFragment) {
            logger.log("MainActivity hide navigation menu")
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            hideNavigationMenu()
        } else {
            logger.log("MainActivity show navigation menu")
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            showNavigationMenu()
        }

        if (savedInstanceState != null && navController.currentDestination?.id == R.id.loginFragment) {
            hideNavigationMenu()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun closeDrawer() {
        drawerLayout.closeDrawer(Gravity.LEFT)
    }

    private fun hideNavigationMenu() {
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun showNavigationMenu() {
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun openLoginFragment() {
        navController.navigate(
            R.id.action_splashFragment_to_loginFragment, null, NavOptions.Builder()
                .setPopUpTo(R.id.splashFragment, true)
                .build()
        )
        hideNavigationMenu()
    }

    override fun jumpToNewsFragment() {
        navController.navigate(
            R.id.action_splashFragment_to_newsFragment, null, NavOptions.Builder()
                .setPopUpTo(R.id.splashFragment, true)
                .build()
        )
    }

    override fun openNewsFragment() {
        navController.navigate(
            R.id.action_loginFragment_to_newsFragment, null, NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build()
        )
    }

    override fun downloadUserData() {
        viewModel.getUserData()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        logger.log("MainActivity onNavigationItemSelected item id: ${item.itemId}")
        when (item.itemId) {
            R.id.friendsFragment -> {
                moveToFriendsFragment()
                closeDrawer()
            }

            R.id.photosFragment -> {
                moveToPhotosFragment()
                closeDrawer()
            }

            R.id.newsFragment -> {
                moveToNewsFragment()
                closeDrawer()
            }

            R.id.twLogin, R.id.fbLogin -> {
                moveToLoginFragment()
                hideNavigationMenu()
                closeDrawer()
            }
            R.id.logout -> {
                viewModel.logOut()
                moveToLoginFragment()
                hideNavigationMenu()
                closeDrawer()

            }
        }
        return true
    }

    private fun moveToLoginFragment() {
        when (navController.currentDestination?.id) {

            R.id.newsFragment -> navController.navigate(
                R.id.action_newsFragment_to_loginFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.newsFragment, true)
                    .build()
            )
            R.id.photosFragment -> navController.navigate(
                R.id.action_photosFragment_to_loginFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.photosFragment, true)
                    .build()
            )
            R.id.friendsFragment -> navController.navigate(
                R.id.action_friendsFragment_to_loginFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.friendsFragment, true)
                    .build()
            )
        }
    }

    private fun moveToFriendsFragment() {
        when (navController.currentDestination?.id) {
            R.id.newsFragment -> navController.navigate(
                R.id.action_newsFragment_to_friendsFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.newsFragment, true)
                    .build()
            )
            R.id.photosFragment -> navController.navigate(
                R.id.action_photosFragment_to_friendsFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.photosFragment, true)
                    .build()
            )
        }
    }

    private fun moveToPhotosFragment() {
        when (navController.currentDestination?.id) {
            R.id.newsFragment -> navController.navigate(
                R.id.action_newsFragment_to_photosFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.newsFragment, true)
                    .build()
            )
            R.id.friendsFragment -> navController.navigate(
                R.id.action_friendsFragment_to_photosFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.friendsFragment, true)
                    .build()
            )
        }
    }

    private fun moveToNewsFragment() {
        when (navController.currentDestination?.id) {
            R.id.photosFragment -> navController.navigate(
                R.id.action_photosFragment_to_newsFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.photosFragment, true)
                    .build()
            )
            R.id.friendsFragment -> navController.navigate(
                R.id.action_friendsFragment_to_newsFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.friendsFragment, true)
                    .build()
            )
        }
    }
}