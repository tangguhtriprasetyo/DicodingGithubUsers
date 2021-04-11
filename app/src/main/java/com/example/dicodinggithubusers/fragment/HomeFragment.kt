package com.example.dicodinggithubusers.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodinggithubusers.activity.DetailActivity
import com.example.dicodinggithubusers.adapter.RvUsersAdapter
import com.example.dicodinggithubusers.databinding.FragmentHomeBinding
import com.example.dicodinggithubusers.model.Users
import com.example.dicodinggithubusers.viewmodel.MainViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var urlUser: String? = null
    private var state: Boolean = false
    private var dataList: Boolean = true

    private lateinit var adapter: RvUsersAdapter
    private lateinit var mainViewModel: MainViewModel

    companion object {
        const val STATE_OUT = "state_out"
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        if (savedInstanceState != null) {
            state = savedInstanceState.getBoolean(STATE_OUT)
        }

        if (!state) {
            Log.d("SAVEDINSTANCE2", savedInstanceState.toString())
            setDataRv()
        }
        adapter = RvUsersAdapter()
        adapter.notifyDataSetChanged()

        binding.rvUsers.layoutManager = LinearLayoutManager(activity)
        binding.rvUsers.adapter = adapter
        binding.rvUsers.setHasFixedSize(true)

        mainViewModel.getListUsers().observe(viewLifecycleOwner, { userItems ->
            if (userItems != null) {

                adapter.setData(userItems)
                binding.rvUsers.visibility = View.VISIBLE
                showLoading(false)
                binding.tvErrorMessage.visibility = View.GONE
            } else {
                val errorMessage: String? = mainViewModel.getMessageError()
                if (errorMessage != null) {
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
                }
                binding.tvErrorMessage.visibility = View.VISIBLE
                binding.rvUsers.visibility = View.INVISIBLE
                showLoading(false)
            }
        })

        adapter.setOnItemClickCallback(object : RvUsersAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                showSelectedUser(data)
            }
        })

        //Callback untuk SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    urlUser = "https://api.github.com/search/users?q=$query"
                    dataList = false
                    setDataRv()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }
        })

        binding.tvErrorMessage.setOnClickListener {
            setDataRv()
        }

        return view
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    //Fungsi Menampilkan Data ke RV
    private fun setDataRv() {
        binding.tvErrorMessage.visibility = View.GONE
        showLoading(true)
        mainViewModel.setListUsers(urlUser, dataList)
    }

    //Fungsi Pilih User dan Pindah Activity dengan Parcelable
    private fun showSelectedUser(data: Users) {

        Toast.makeText(activity, "${data.username}", Toast.LENGTH_SHORT).show()
        val intentDetail = Intent(activity, DetailActivity::class.java)
        intentDetail.putExtra(DetailActivity.EXTRA_USER, data)
        startActivity(intentDetail)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_OUT, true)
    }

}