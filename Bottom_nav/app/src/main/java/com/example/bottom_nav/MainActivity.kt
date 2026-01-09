package com.example.bottom_nav

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var toolbarText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        drawerLayout = findViewById(R.id.drawer_layout)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        toolbarText = findViewById(R.id.textView)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        // Setup Toolbar
        setSupportActionBar(toolbar)

        // Setup Drawer Toggle
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set navigation drawer listener
        navigationView.setNavigationItemSelectedListener(this)

        // Initialize fragments
        val firstFragment = FirstFragment()
        val secondFragment = SecondFragment()
        val thirdFragment = FragmentThird()
        val newsFragment = NewsFragment()
        val postFragment = PostFragment()

        // Set the username from session on toolbar and drawer
        val username = UserSession.username
        toolbarText.text = username
        val uid = UserSession.uid


        val headerView = navigationView.getHeaderView(0)
        val usernameTextView: TextView = headerView.findViewById(R.id.username)
        usernameTextView.text = "Username: $username"
        val uidTextView: TextView = headerView.findViewById(R.id.uid)
        uidTextView.text = "UID: $uid"

        // Show profile fragment by default
        setCurrentFragment(firstFragment)
        bottomNavigationView.selectedItemId = R.id.home

        // Bottom Navigation click events to switch fragments
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentFragment(firstFragment)
                R.id.heroes -> setCurrentFragment(secondFragment)
                R.id.profile -> setCurrentFragment(thirdFragment)
                R.id.news -> setCurrentFragment(newsFragment)
                R.id.addpost -> setCurrentFragment(postFragment)
            }
            true
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> setCurrentFragment(FirstFragment())
            R.id.nav_heroes -> setCurrentFragment(SecondFragment())
            R.id.nav_profile -> setCurrentFragment(FragmentThird())
            R.id.nav_about -> setCurrentFragment(AboutFragment())
            R.id.nav_logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure you want to logout?")
                    .setTitle("Logout")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                builder.create().show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private var exitConfirmed = false
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (exitConfirmed) {
                super.onBackPressed()
                return
            }
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Do you want to exit?")
                .setTitle("Close the app")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ -> finish() }
                .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
            builder.create().show()
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }
}
