package com.example.dicodinggithubusers.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.dicodinggithubusers.R
import com.example.dicodinggithubusers.databinding.ActivityMainBinding
import com.example.dicodinggithubusers.fragment.HomeFragment
import com.example.dicodinggithubusers.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    companion object {
        const val HOME_FRAGMENT_TAG = "home_fragment_tag"
        const val PROFILE_FRAGMENT_TAG = "profile_fragment_tag"
        const val PREFERENCE_FRAGMENT_TAG = "preference_fragment_tag"
    }

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()

        setReminder()

        if (savedInstanceState != null) {
            supportFragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG)
                ?.let { setCurrentFragment(it, HOME_FRAGMENT_TAG) }
        } else {

            setCurrentFragment(homeFragment, HOME_FRAGMENT_TAG)
        }

        //Pilih Fragment Berdasarkan Menu yang Dipilih
        binding.bottomNavigationBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    setCurrentFragment(homeFragment, HOME_FRAGMENT_TAG)
                }

                R.id.profile -> {
                    setCurrentFragment(profileFragment, PROFILE_FRAGMENT_TAG)
                }
            }
            true
        }

    }

    private fun setReminder() {

    }

    private fun setCurrentFragment(fragment: Fragment, fragmentTag: String) {
        supportFragmentManager.commit {
            replace(R.id.display_fragment, fragment, fragmentTag)
        }
    }

}