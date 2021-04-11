package com.example.dicodinggithubusers.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.dicodinggithubusers.R
import com.example.dicodinggithubusers.databinding.ActivityMainBinding
import com.example.dicodinggithubusers.fragment.HomeFragment
import com.example.dicodinggithubusers.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    companion object {
        const val HOME_FRAGMENT_TAG = "home_fragment_tag"
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var homeFragment: Fragment? = null
        val profileFragment = ProfileFragment()

        if (savedInstanceState != null) {
            supportFragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG)
        } else if (homeFragment == null) {
            homeFragment = HomeFragment()
            setCurrentFragment(homeFragment)
        }

        //Pilih Fragment Berdasarkan Menu yang Dipilih
        binding.bottomNavigationBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> if (savedInstanceState != null) {
                    supportFragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG)
                } else if (homeFragment == null) {
                    homeFragment = HomeFragment()
                    setCurrentFragment(homeFragment as HomeFragment)
                }
                R.id.profile -> setCurrentFragment(profileFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.display_fragment, fragment, HOME_FRAGMENT_TAG)
            commit()
        }
    }

}