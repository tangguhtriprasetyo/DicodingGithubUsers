package com.example.dicodinggithubusers.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.dicodinggithubusers.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private val TIME_OUT: Long = 2000

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Jeda waktu 2 detik untuk SplashScreen ke Home
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, TIME_OUT)
    }
}