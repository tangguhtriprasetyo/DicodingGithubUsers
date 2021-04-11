package com.example.dicodinggithubusers.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodinggithubusers.activity.DetailActivity
import com.example.dicodinggithubusers.adapter.RvUsersAdapter
import com.example.dicodinggithubusers.databinding.FragmentFollowersBinding
import com.example.dicodinggithubusers.model.Users
import com.example.dicodinggithubusers.viewmodel.MainViewModel

class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private var state: Boolean = false

    private lateinit var adapter: RvUsersAdapter
    private lateinit var mainViewModel: MainViewModel

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        const val STATE_OUT = "state_out"

        @JvmStatic
        fun newInstance(index: Int) =
                FollowersFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_SECTION_NUMBER, index)
                    }
                }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        val view = binding.root

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        adapter = RvUsersAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowers.adapter = adapter
        binding.rvFollowers.setHasFixedSize(true)

        val dataUser = activity?.intent?.getParcelableExtra<Users>(DetailActivity.EXTRA_USER) as Users
        val userName = dataUser.username
        val urlFollowers = "https://api.github.com/users/$userName/followers"
        val urlFollowing = "https://api.github.com/users/$userName/following"

        if (savedInstanceState != null) {
            state = savedInstanceState.getBoolean(STATE_OUT)
        }

        when (arguments?.getInt(ARG_SECTION_NUMBER, 0)) {
            1 -> if (!state) {
                setDataRv(urlFollowers)
            }
            2 -> if (!state) {
                setDataRv(urlFollowing)
            }
        }

        adapter.setOnItemClickCallback(object : RvUsersAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                showSelectedUser(data)
            }
        })

        binding.tvErrorMessage.setOnClickListener {
            when (arguments?.getInt(ARG_SECTION_NUMBER, 0)) {
                1 -> if (!state) {
                    setDataRv(urlFollowers)
                }
                2 -> if (!state) {
                    setDataRv(urlFollowing)
                }
            }
        }

        mainViewModel.getListUsers().observe(viewLifecycleOwner, { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                showLoading(false)
                binding.tvErrorMessage.visibility = View.GONE
            } else {
                val errorMessage: String? = mainViewModel.getMessageError()
                if (errorMessage != null) {
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
                }
                binding.tvErrorMessage.visibility = View.VISIBLE
                showLoading(false)
            }
        })

        return view
    }

    private fun setDataRv(url: String) {
        binding.tvErrorMessage.visibility = View.GONE
        showLoading(true)
        mainViewModel.setListUsers(url, true)
    }

    private fun showSelectedUser(data: Users) {

        Toast.makeText(activity, "${data.username}", Toast.LENGTH_SHORT).show()
        val intentDetail = Intent(activity, DetailActivity::class.java)
        intentDetail.putExtra(DetailActivity.EXTRA_USER, data)
        startActivity(intentDetail)
    }


    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(HomeFragment.STATE_OUT, true)
    }

}