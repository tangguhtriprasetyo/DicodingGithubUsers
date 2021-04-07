package com.example.dicodinggithubusers.activity

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.dicodinggithubusers.R
import com.example.dicodinggithubusers.adapter.SectionPageAdapter
import com.example.dicodinggithubusers.databinding.ActivityDetailBinding
import com.example.dicodinggithubusers.loadImage
import com.example.dicodinggithubusers.model.Users
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.followings
        )
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTabLayout()
        initView()
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initTabLayout() {
        val sectionPageAdapter = SectionPageAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPageAdapter
        val tabs: TabLayout = binding.tabDetail

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    // Ambil data dari Parcelable dan Assign ke Layout
    private fun initView() {
        val user = intent.getParcelableExtra<Users>(EXTRA_USER) as Users
        binding.tvTitleDetail.text = user.username
        binding.tvDetailName.text = user.name
        binding.tvDetailUsername.text = user.username
        binding.tvDetailLocation.text = user.location
        binding.tvDetailCompany.text = user.company
        binding.tvDetailRepository.text = user.repository

        Glide.with(binding.imgDetailAvatar)
            .load(user.avatar)
            .into(binding.imgDetailAvatar)

        binding.imgDetailAvatar.loadImage(user.avatar)

    }
}