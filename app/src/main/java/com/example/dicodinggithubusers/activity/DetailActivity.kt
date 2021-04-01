package com.example.dicodinggithubusers.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.dicodinggithubusers.databinding.ActivityDetailBinding
import com.example.dicodinggithubusers.model.Users

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    // Ambil data dari Parcelable dan Assign ke Layout
    private fun initView() {
        val user = intent.getParcelableExtra<Users>(EXTRA_USER) as Users
        binding.tvTitleDetail.text = user.username
        binding.tvDetailName.text = user.name
        binding.tvDetailUsername.text = user.username
        binding.tvDetailLocation.text = user.location
        binding.tvDetailCompany.text = user.company
        binding.tvDetailFollower.text = user.follower
        binding.tvDetailFollowing.text = user.following
        binding.tvDetailRepository.text = user.repository

        Glide.with(binding.imgDetailAvatar)
                .load(user.avatar)
                .into(binding.imgDetailAvatar)
    }
}