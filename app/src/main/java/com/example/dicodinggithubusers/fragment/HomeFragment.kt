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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodinggithubusers.R
import com.example.dicodinggithubusers.activity.DetailActivity
import com.example.dicodinggithubusers.adapter.RvUsersAdapter
import com.example.dicodinggithubusers.databinding.FragmentHomeBinding
import com.example.dicodinggithubusers.model.SearchResponse
import com.example.dicodinggithubusers.model.Users
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.msebera.android.httpclient.Header

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var list: ArrayList<Users> = arrayListOf()

    private val client = AsyncHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.rvUsers.setHasFixedSize(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserListData(null)

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    //Fungsi Menampilkan Data ke RV
    private fun showRecyclerData() {
        binding.rvUsers.layoutManager = LinearLayoutManager(activity)
        val rvUsersAdapter = RvUsersAdapter(list)
        binding.rvUsers.adapter = rvUsersAdapter

        rvUsersAdapter.setOnItemClickCallback(object : RvUsersAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                showSelectedUser(data)
            }
        })

        //Callback untuk SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    list.clear()
                    getUserListData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                rvUsersAdapter.filter.filter(newText)
                return false
            }
        })
    }

    //Fungsi Pilih User dan Pindah Activity dengan Parcelable
    private fun showSelectedUser(data: Users) {

        Toast.makeText(activity, "Kamu memilih ${data.username}", Toast.LENGTH_SHORT).show()
        val intentDetail = Intent(activity, DetailActivity::class.java)
        intentDetail.putExtra(DetailActivity.EXTRA_USER, data)
        startActivity(intentDetail)

    }

    private fun getUserListData(query: String?) {
        showLoading(true)

        val url: String = if (query != null) {
            "https://api.github.com/search/users?q=$query"
        } else {
            "https://api.github.com/users"
        }
        Log.d("URL Endpoint: ", url)

        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", getString(R.string.token_api))
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
            ) {
                showLoading(false)

                val result = String(responseBody!!)
                Log.d("onSuccessListUser: ", result)

                try {
                    val moshi = Moshi.Builder()
                            .addLast(KotlinJsonAdapterFactory())
                            .build()

                    if (query != null) {
                        val jsonAdapter = moshi.adapter(SearchResponse::class.java)
                        val response = jsonAdapter.fromJson(result)
                        response?.let {
                            for (i in it.items.indices) {
                                val userData = it.items[i]
                                val urlUsers = userData.url
                                getUserDetailData(urlUsers.toString())
                            }
                        }
                    } else {
                        val listType =
                                Types.newParameterizedType(List::class.java, Users::class.java)
                        val jsonAdapter: JsonAdapter<List<Users>> = moshi.adapter(listType)
                        val response = jsonAdapter.fromJson(result)
                        response?.let {
                            for (i in response.indices) {
                                val userData = response[i]
                                val urlUsers = userData.url
                                getUserDetailData(urlUsers.toString())
                            }
                        }
                    }

                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
            ) {
                Log.d("onFailureGetUser", error?.message.toString())
                showLoading(false)
                Toast.makeText(activity, error?.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getUserDetailData(url: String) {
        Log.d("URL: ", url)
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", getString(R.string.token_api))
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
            ) {
                showLoading(false)

                val result = String(responseBody!!)
                Log.d("onSuccessDetailUser: ", result)

                try {
                    val moshi = Moshi.Builder()
                            .addLast(KotlinJsonAdapterFactory())
                            .build()

                    val jsonAdapter = moshi.adapter(Users::class.java)
                    val response = jsonAdapter.fromJson(result)

                    response?.let {
                        val users = Users()
                        users.username = it.username
                        users.name = it.name
                        users.repository = it.repository
                        users.company = it.company
                        users.follower = it.follower
                        users.following = it.following
                        users.avatar = it.avatar
                        users.location = it.location
                        list.add(users)
                        Log.d("TOTAL LIST", list.size.toString())
                    }
                    binding.rvUsers.apply {
                        showRecyclerData()
                    }

                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
            ) {
                Log.d("onFailureGetDetail", error?.message.toString())
                showLoading(false)
                Toast.makeText(activity, error?.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}