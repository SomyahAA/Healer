package com.example.healer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import coil.load
import com.example.healer.R
import com.example.healer.databinding.ActivityMainBinding
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var menu: Menu

    private val mainVM: MainVM by lazy { ViewModelProvider(this)[MainVM::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // to find the nav controller from the nav host fragment

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.side_navigation)
        toolbar = findViewById(R.id.tool_bar)


        navController = findNavController(R.id.fragmentContainerView)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
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

        if (mainVM.currentUserExist()) {

            mainVM.getPhotoFromStorage().observe(this) {
                val v: CircleImageView = findViewById(R.id.headerPhoto)
                v.load(it)
            }
            lifecycleScope.launch {
                mainVM.getHeaderNameFromFirebase().observe(this@MainActivity) {
                    val headerName = findViewById<TextView>(R.id.headerName)
                    headerName.text = it
                }
            }
        }
        mainVM.setUpRecurringWork(baseContext)
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
                lifecycleScope.launch {
                    if (mainVM.userTypeIsUser()) {
                        navController.navigate(R.id.homeFragment)
                    } else {
                        navController.navigate(R.id.psyHomeFragment)
                    }
                }
            }
            R.id.nav_login -> { navController.navigate(R.id.loginFragment)
                if (mainVM.currentUserExist()) {
                    menu.findItem(R.id.nav_logout).isVisible = true
                    menu.findItem(R.id.nav_profile).isVisible = true
                    menu.findItem(R.id.nav_login).isVisible = false
                }
            }
            R.id.nav_logout -> {
                mainVM.signOut()
                menu.findItem(R.id.nav_logout).isVisible = false
                menu.findItem(R.id.nav_profile).isVisible = false
                menu.findItem(R.id.nav_login).isVisible = true
            }
            //R.id.nav_chats -> navController.navigate(R.id.chatsFragment)
            R.id.nav_profile -> {
                lifecycleScope.launch {
                    if (mainVM.userTypeIsUser()) {
                        navController.navigate(R.id.userProfileFragment)
                    } else {
                        navController.navigate(R.id.psychologistProfileFragment)
                    }
                }
            }
            R.id.nav_setting -> navController.navigate(R.id.settingFragment)
            R.id.nav_workWithUs -> navController.navigate(R.id.psychologistRegisterFragment)
            R.id.nav_appointments ->
                if (mainVM.currentUserExist()) {
                    lifecycleScope.launch {
                    if (mainVM.userTypeIsUser()) {
                        navController.navigate(R.id.appointmentsFragment)
                    }else{
                        navController.navigate(R.id.psyBookedAppointments)
                    }
                    }
                } else {
                    navController.navigate(R.id.noAppointmentsFragment)
                }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }



}
