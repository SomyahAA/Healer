package com.example.healer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.healer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // to find the nav controller from the nav host fragment


        drawerLayout=findViewById(R.id.drawer_layout)
        navigationView=findViewById(R.id.side_navigation)
        toolbar=findViewById(R.id.tool_bar)
        navController = findNavController(R.id.fragmentContainerView)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        navigationView.bringToFront()
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_home)

        menu = navigationView.menu
        menu.findItem(R.id.nav_logout).isVisible = false
        menu.findItem(R.id.nav_profile).isVisible = false
    }

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
            R.id.nav_home -> {
                navController.navigate(R.id.homeFragment)
            }

            R.id.nav_login -> {
                navController.navigate(R.id.loginFragment)
                menu.findItem(R.id.nav_logout).isVisible = true
                menu.findItem(R.id.nav_profile).isVisible = true
                menu.findItem(R.id.nav_login).isVisible = false
            }

            R.id.nav_logout -> {
                menu.findItem(R.id.nav_logout).isVisible = false
                menu.findItem(R.id.nav_profile).isVisible = false
                menu.findItem(R.id.nav_login).isVisible = true
            }
            R.id.nav_chats->{
             navController.navigate(R.id.chatsFragment)
            }
            R.id.nav_appointments->{
                navController.navigate(R.id.appointmentsFragment)
            }
            R.id.nav_profile->{
                navController.navigate(R.id.profileFragment)
            }
            R.id.nav_logout->{
                navController.navigate(R.id.psychologistRegisterFragment)
            }
            R.id.nav_setting -> {
                navController.navigate(R.id.settingFragment)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}