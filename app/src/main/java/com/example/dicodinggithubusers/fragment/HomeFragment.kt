package com.example.dicodinggithubusers.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodinggithubusers.R
import com.example.dicodinggithubusers.UserData
import com.example.dicodinggithubusers.activity.DetailActivity
import com.example.dicodinggithubusers.adapter.RvUsersAdapter
import com.example.dicodinggithubusers.databinding.FragmentHomeBinding
import com.example.dicodinggithubusers.model.Users

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var list: ArrayList<Users> = arrayListOf()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.rvUsers.setHasFixedSize(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.addAll(UserData.listUser)
        Log.d("List User", list.size.toString())
        binding.rvUsers.apply {
            showRecyclerData()
        }
    }

    private fun showRecyclerData() {
        binding.rvUsers.layoutManager = LinearLayoutManager(activity)
        val rvUsersAdapter = RvUsersAdapter(list)
        binding.rvUsers.adapter = rvUsersAdapter

        rvUsersAdapter.setOnItemClickCallback(object : RvUsersAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                showSelectedUser(data)
            }
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                rvUsersAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun showSelectedUser(data: Users) {

        Toast.makeText(activity, "Kamu memilih ${data.username}", Toast.LENGTH_SHORT).show()
        val intentDetail = Intent(activity, DetailActivity::class.java)
        intentDetail.putExtra(DetailActivity.EXTRA_USER, data)
        startActivity(intentDetail)

    }
}