package com.practice.socialclient.ui

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
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
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.model.dto.UserInfo
import com.practice.socialclient.ui.arch.FragmentContract
import com.practice.socialclient.ui.friends.item_fragment.ItemFriendsContract
import com.practice.socialclient.ui.login.LoginContract
import com.practice.socialclient.ui.news.item_fragment.ItemNewsContract
import com.practice.socialclient.ui.photos.item_fragment.ItemPhotosContract
import com.practice.socialclient.ui.splash.SplashContract

class MainActivity : AppCompatActivity(), FragmentContract.Host, SplashContract.Host,
    LoginContract.Host,
    ItemNewsContract.Host, ItemPhotosContract.Host, ItemFriendsContract.Host {

    private val logger: Log = Logger.withTag("MyLog")
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navigationView: NavigationView
    private var actionBar: ActionBar? = null
    private lateinit var navMenu: Menu
    private lateinit var headerBinding: NavHeaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_content_view)
        navigationView.setupWithNavController(navController)
        headerBinding = NavHeaderBinding.inflate(layoutInflater)
        navigationView.addHeaderView(headerBinding.root)
        val topLevelDestinations: MutableSet<Int> = HashSet()
        topLevelDestinations.add(R.id.newsFragment)
        topLevelDestinations.add(R.id.friendsFragment)
        topLevelDestinations.add(R.id.photosFragment)
        appBarConfiguration = AppBarConfiguration(topLevelDestinations, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        actionBar = supportActionBar
        navMenu = navigationView.menu

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

    override fun closeDrawer() {
        drawerLayout.closeDrawer(Gravity.LEFT)
    }

    override fun hideNavigationMenu() {
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

    override fun moveToLoginFragment() {
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
                R.id.action_friendsPagerFragment_to_loginFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.friendsFragment, true)
                    .build()
            )
        }
    }

    override fun moveToFriendsFragment() {
        when (navController.currentDestination?.id) {
            R.id.newsFragment -> navController.navigate(
                R.id.action_newsFragment_to_friendsFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.newsFragment, true)
                    .build()
            )
            R.id.photosFragment -> navController.navigate(
                R.id.action_photosFragment_to_friendsPagerFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.photosFragment, true)
                    .build()
            )
        }
    }

    override fun moveToPhotosFragment() {
        when (navController.currentDestination?.id) {
            R.id.newsFragment -> navController.navigate(
                R.id.action_newsFragment_to_photosFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.newsFragment, true)
                    .build()
            )
            R.id.friendsFragment -> navController.navigate(
                R.id.action_friendsPagerFragment_to_photosFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.friendsFragment, true)
                    .build()
            )
        }
    }

    override fun moveToNewsFragment() {
        when (navController.currentDestination?.id) {
            R.id.photosFragment -> navController.navigate(
                R.id.action_photosFragment_to_newsFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.photosFragment, true)
                    .build()
            )
            R.id.friendsFragment -> navController.navigate(
                R.id.action_friendsPagerFragment_to_newsFragment, null, NavOptions.Builder()
                    .setPopUpTo(R.id.friendsFragment, true)
                    .build()
            )
        }
    }

    override fun setFbUserData(user: UserInfo) {
        logger.log("MainActivity setFbUserData user: $user")
        if (user.name.isEmpty() && user.iconUrl.isEmpty()) {
            findViewById<CardView>(R.id.cvFbUserPhoto)?.let { it.visibility = View.INVISIBLE }
            navMenu.findItem(R.id.fbLogin).isVisible = true
            headerBinding.fbUserData = UserInfo(getString(R.string.login_to_facebook), "")
        } else {
            findViewById<CardView>(R.id.cvFbUserPhoto)?.let { it.visibility = View.VISIBLE }
            navMenu.findItem(R.id.fbLogin).isVisible = false
            headerBinding.fbUserData = user
        }
    }

    override fun setTwUserData(user: UserInfo) {
        logger.log("MainActivity setTwUserData user: $user")
        if (user.name.isEmpty() && user.iconUrl.isEmpty()) {
            findViewById<CardView>(R.id.cvTwUserPhoto)?.let { it.visibility = View.INVISIBLE }
            navMenu.findItem(R.id.twLogin).isVisible = true
            headerBinding.twUserData = UserInfo(getString(R.string.login_to_twitter), "")
        } else {
            findViewById<CardView>(R.id.cvTwUserPhoto)?.let { it.visibility = View.VISIBLE }
            navMenu.findItem(R.id.twLogin).isVisible = false
            headerBinding.twUserData = user
        }
    }

    override fun setNavListener(listener: NavigationView.OnNavigationItemSelectedListener){
        navigationView.setNavigationItemSelectedListener(listener)
    }
}