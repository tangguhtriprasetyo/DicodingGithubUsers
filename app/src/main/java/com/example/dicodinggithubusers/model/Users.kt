package com.example.dicodinggithubusers.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
    var username: String = "",
    var name: String = "",
    var avatar: String = "",
    var company: String = "",
    var location: String = "",
    var repository: String = "",
    var follower: String = "",
    var following: String = "",
) : Parcelable
