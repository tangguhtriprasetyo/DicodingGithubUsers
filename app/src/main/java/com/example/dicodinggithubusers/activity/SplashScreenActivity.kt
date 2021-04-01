package com.example.dicodinggithubusers.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.dicodinggithubusers.R
import com.example.dicodinggithubusers.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private val TIME_OUT:Long = 2000

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, TIME_OUT)
    }
}