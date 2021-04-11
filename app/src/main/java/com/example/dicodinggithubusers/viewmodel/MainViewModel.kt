package com.example.dicodinggithubusers.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodinggithubusers.model.SearchResponse
import com.example.dicodinggithubusers.model.Users
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.msebera.android.httpclient.Header

class MainViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<Users>>()
    val listItems = ArrayList<Users>()

    fun setListUsers(query: String?) {
        listItems.clear()
        getData(query)
    }

    fun getListUsers(): LiveData<ArrayList<Users>> {
        return listUsers
    }

    private fun getData(query: String?) {

        val client = AsyncHttpClient()
        val url: String = if (query != null) {
            "https://api.github.com/search/users?q=$query"
        } else {
            "https://api.github.com/users"
        }
        Log.d("URL Endpoint: ", url)

        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
            ) {

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
                            it.items.indices.forEach { i ->
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
                            response.indices.forEach { i ->
                                val userData = response[i]
                                val urlUsers = userData.url
                                getUserDetailData(urlUsers.toString())
                            }
                        }
                    }

                } catch (e: Exception) {
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
            }
        })
    }

    private fun getUserDetailData(urlUsers: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(urlUsers, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
            ) {

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
                        listItems.add(users)
                    }
                    listUsers.postValue(listItems)


                } catch (e: Exception) {
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
            }
        })
    }
}