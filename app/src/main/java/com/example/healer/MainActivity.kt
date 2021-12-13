package com.example.healer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.healer.databinding.ActivityMainBinding
import android.widget.TextView

import android.view.Menu
import android.view.MenuItem

import com.google.android.material.navigation.NavigationView

import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat














class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    lateinit var menu: Menu


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*---------------------Hooks------------------------*/
        drawerLayout=findViewById(R.id.drawer_layout)
        navigationView=findViewById(R.id.side_navigation)

        toolbar=findViewById(R.id.tool_bar)

        navigationView.bringToFront()

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.homeFragment)
    }



//    private fun setupNavigation() {
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.loginFragment -> {
//                    binding.sideNavigation.visibility = View.GONE
//                    supportActionBar?.hide()
//                }
//                R.id.homeFragment -> {
//                    binding.sideNavigation.visibility = View.VISIBLE
//                    supportActionBar?.hide()
//                }
//                R.id.userRegister -> {
//                    binding.sideNavigation.visibility = View.GONE
//                    supportActionBar?.hide()
//                }
//                R.id.registerPsy -> {
//                    binding.sideNavigation.visibility = View.GONE
//                    supportActionBar?.hide()
//                }
//                else -> {
//                    binding.sideNavigation.visibility = View.GONE
//                    supportActionBar?.show()
//                }
//            }
//        }
//    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.homeFragment -> {
            }
            R.id.loginFragment -> {
                menu.findItem(R.id.nav_logout).isVisible = true
                menu.findItem(R.id.nav_profile).isVisible = true
                menu.findItem(R.id.nav_login).isVisible = false
            }
            R.id.nav_logout -> {
                menu.findItem(R.id.nav_logout).isVisible = false
                menu.findItem(R.id.nav_profile).isVisible = false
                menu.findItem(R.id.nav_login).isVisible = true
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}