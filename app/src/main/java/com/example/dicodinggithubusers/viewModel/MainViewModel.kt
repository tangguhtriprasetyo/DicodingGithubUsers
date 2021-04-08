package com.example.dicodinggithubusers.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodinggithubusers.model.Users

class MainViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<Users>>()

    fun setListUsers(query: String) {
        //API
    }

    fun getListUsers(): LiveData<ArrayList<Users>> {
        return listUsers
    }
}