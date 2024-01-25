package com.example.paperwalls

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class SplashActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Get references to TextView and ImageView
        val appNameTextView: TextView = findViewById(R.id.appName_text)
        val logoImageView: ImageView = findViewById(R.id.logo)
        val byAuthorTextView: TextView = findViewById(R.id.author_text)

        // Introduce a delay before starting the fade-out animation
        Handler().postDelayed({
            // Load fade-out animation
            val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)

            // Apply fade-out animation to TextView and ImageView
            appNameTextView.startAnimation(fadeOut)
            logoImageView.startAnimation(fadeOut)
            byAuthorTextView.startAnimation(fadeOut)

            // Set AnimationListener on fade-out animation
            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    // Set the visibility of elements to INVISIBLE
                    appNameTextView.visibility = View.INVISIBLE
                    logoImageView.visibility = View.INVISIBLE
                    byAuthorTextView.visibility = View.INVISIBLE

                    // Delay for an additional 300 milliseconds
                    Handler().postDelayed({
                        // Start MainActivity
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish() // finish the current activity
                    }, 100)
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })

        }, 500) // Delay for 500 milliseconds before starting the fade-out animation
    }
}