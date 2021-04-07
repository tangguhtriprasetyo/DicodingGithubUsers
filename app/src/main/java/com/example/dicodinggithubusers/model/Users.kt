package com.example.dicodinggithubusers.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
        @Json(name = "login")
        var username: String? = null,

        var name: String? = null,

        @Json(name = "avatar_url")
        var avatar: String? = null,

        var company: String? = null,

        var location: String? = null,

        @Json(name = "public_repos")
        var repository: String? = null,

        @Json(name = "followers")
        var follower: String? = null,

        var following: String? = null,

        var url: String? = null
) : Parcelable
