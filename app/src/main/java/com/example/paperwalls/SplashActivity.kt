package com.example.paperwalls

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val constraintLayout: ConstraintLayout = findViewById(R.id.splash_screen)

        // Introduce a delay before starting the fade-out animation
        Handler().postDelayed({

            // Apply fade-out animation
            val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            constraintLayout.startAnimation(fadeOut)

            // Start MainActivity once the animation is done
            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {

                    constraintLayout.visibility = View.INVISIBLE

                    // add a delay before starting the main activity
                    Handler().postDelayed({
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java),null)
                        finish()
                        overridePendingTransition(0, 0)
                    }, 100)
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })

        }, 500)
    }
}