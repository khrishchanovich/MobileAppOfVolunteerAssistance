package com.example.volunteerassistance

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true)

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (isFirstLaunch) {
            sharedPreferences.edit().putBoolean("isFirstLaunch", false).apply()
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        } else if (currentUser != null) {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }

        finish()
    }
}
