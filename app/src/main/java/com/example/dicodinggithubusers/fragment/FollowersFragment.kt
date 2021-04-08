package com.example.dicodinggithubusers.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodinggithubusers.R
import com.example.dicodinggithubusers.activity.DetailActivity
import com.example.dicodinggithubusers.adapter.RvUsersAdapter
import com.example.dicodinggithubusers.databinding.FragmentFollowersBinding
import com.example.dicodinggithubusers.model.Users
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.msebera.android.httpclient.Header

class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private var list: ArrayList<Users> = arrayListOf()

    private val client = AsyncHttpClient()

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

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
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.rvFollowers.setHasFixedSize(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataUser = activity?.intent?.getParcelableExtra<Users>(DetailActivity.EXTRA_USER) as Users
        val userName = dataUser.username
        val urlFollowers = "https://api.github.com/users/$userName/followers"
        val urlFollowing = "https://api.github.com/users/$userName/following"

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        when (index) {
            1 -> getUserListData(urlFollowers)
            2 -> getUserListData(urlFollowing)
        }

    }

    private fun getUserListData(query: String?) {
        showLoading(true)

        Log.d("GetUserURL", query.toString())
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", getString(R.string.token_api))
        client.get(query, object : AsyncHttpResponseHandler() {
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
                    binding.rvFollowers.apply {
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

    //Fungsi Menampilkan Data ke RV
    private fun showRecyclerData() {
        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        val rvUsersAdapter = RvUsersAdapter(list)
        binding.rvFollowers.adapter = rvUsersAdapter

        rvUsersAdapter.setOnItemClickCallback(object : RvUsersAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(data: Users) {

        Toast.makeText(activity, "Kamu memilih ${data.username}", Toast.LENGTH_SHORT).show()
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
}