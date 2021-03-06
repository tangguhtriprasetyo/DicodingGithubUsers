package com.example.dicodinggithubusers.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodinggithubusers.BuildConfig
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

    val listUsers = MutableLiveData<ArrayList<Users>?>()
    val listItems = ArrayList<Users>()
    var errorMessage: String? = null

    fun setListUsers(query: String?, dataList: Boolean) {
        listItems.clear()
        getData(query, dataList)
    }

    fun getListUsers(): LiveData<ArrayList<Users>?> {
        return listUsers
    }

    fun getMessageError(): String? {
        return errorMessage
    }

    private fun getData(query: String?, dataList: Boolean) {

        val client = AsyncHttpClient()
        val url: String = query ?: "https://api.github.com/users"
        Log.d("URL Endpoint: ", url)

        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
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
                    if (result == "{\"total_count\":0,\"incomplete_results\":false,\"items\":[]}") {
                        errorMessage = "Data Tidak Ditemukan"
                        listUsers.postValue(null)
                    } else {

                        val moshi = Moshi.Builder()
                                .addLast(KotlinJsonAdapterFactory())
                                .build()

                        if (query != null && !dataList) {
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

                            Log.d("tes: ", result)
                            val listType =
                                    Types.newParameterizedType(List::class.java, Users::class.java)
                            val jsonAdapter: JsonAdapter<List<Users>> = moshi.adapter(listType)
                            val response = jsonAdapter.fromJson(result)
                            Log.d("ResponseSearch: ", response?.size.toString())
                            response?.let {
                                response.indices.forEach { i ->
                                    val userData = response[i]
                                    val urlUsers = userData.url
                                    getUserDetailData(urlUsers.toString())
                                }
                            }
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    errorMessage = e.message
                    listUsers.postValue(null)
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
            ) {
                errorMessage = error?.message
                Log.d("onFailureGetUser", errorMessage.toString())
                listUsers.postValue(null)
            }
        })
    }

    private fun getUserDetailData(urlUsers: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
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
                    errorMessage = e.message
                    e.printStackTrace()
                    listUsers.postValue(null)
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
            ) {
                errorMessage = error?.message
                Log.d("onFailureGetDetail", errorMessage.toString())
                listUsers.postValue(null)
            }
        })
    }
}