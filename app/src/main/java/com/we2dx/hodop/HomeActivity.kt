package com.we2dx.hodop

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mSeacrhButton : AppCompatImageButton
    private lateinit var mNotificationFrameLay : FrameLayout
    private lateinit var mNotificationBadge : AppCompatTextView
    private lateinit var mLoginButton : AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.home_toolbar)
        setSupportActionBar(toolbar)

        mSeacrhButton = toolbar.findViewById(R.id.search_button)
        mNotificationBadge = toolbar.findViewById(R.id.notification_badge_text)
        mNotificationFrameLay = toolbar.findViewById(R.id.user_notification_bell)
        mLoginButton = toolbar.findViewById(R.id.toolbar_login_button)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id==R.id.nav_home){
                if (mSeacrhButton.isVisible){
                    mSeacrhButton.visibility = View.GONE
                }
            }else if (destination.id==R.id.nav_traffic_updates){
                mSeacrhButton.visibility = View.VISIBLE
                mNotificationFrameLay.visibility = View.VISIBLE

            }
        }
        supportActionBar?.title = ""
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_traffic_updates, R.id.nav_newsletter,
                R.id.nav_invite_friends, R.id.nav_about_app, R.id.nav_settings
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
//        navView.setNavigationItemSelectedListener { item ->
//            if (item.itemId == R.id.nav_log_out){
//
//            }
//
//        }
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
