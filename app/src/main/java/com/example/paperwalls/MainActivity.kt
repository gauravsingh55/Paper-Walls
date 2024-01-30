package com.example.paperwalls

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import com.example.paperwalls.databinding.ActivityMainBinding
import nl.joery.animatedbottombar.AnimatedBottomBar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomBar: AnimatedBottomBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomBar = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        bottomBar.onTabSelected = { tab ->
            when (tab.id) {
                R.id.navigation_home -> navController.navigate(R.id.navigation_home)
                R.id.navigation_walls -> navController.navigate(R.id.navigation_walls)
                R.id.navigation_profile -> navController.navigate(R.id.navigation_profile)
            }
        }

        bottomBar.onTabReselected = { tab ->
            // Handle tab reselected event
        }



        // Fade in effect
        val constraintLayout: ConstraintLayout = findViewById(R.id.container)
        val fadeInActivity = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        constraintLayout.startAnimation(fadeInActivity)
    }
}
