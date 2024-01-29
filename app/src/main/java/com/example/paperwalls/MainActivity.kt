package com.example.paperwalls

import android.os.Bundle
import android.view.animation.AnimationUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.paperwalls.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Passing each menu ID as a set of Ids
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_walls, R.id.navigation_profile
            )
        )
        navView.setupWithNavController(navController)

        // Find Create a custom layout for the Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val customToolbar = layoutInflater.inflate(R.layout.custom_toolbar, null)
        toolbar.addView(customToolbar)

        // Customize other Toolbar settings as needed
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Fade in effect
        val constraintLayout: ConstraintLayout = findViewById(R.id.container)
        val fadeInActivity = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        constraintLayout.startAnimation(fadeInActivity)
    }
}
