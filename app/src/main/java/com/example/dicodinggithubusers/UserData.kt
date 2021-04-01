package com.example.dicodinggithubusers

import com.example.dicodinggithubusers.model.Users

object UserData {
    private val usernames = arrayOf(
            "JakeWharton",
            "amitshekhariitbhu",
            "romainguy",
            "chrisbanes",
            "tipsy",
            "ravi8x",
            "jasoet",
            "budioktaviyan",
            "hendisantika",
            "sidiqpermana"
    )

    private val names = arrayOf(
            "Jake Wharton",
            "Amit Shekhar",
            "Romain Guy",
            "Chris Banes",
            "David",
            "Ravi Tamada",
            "Deny Prasetyo",
            "Budi Oktaviyan",
            "Hendi Santika",
            "Sidiq Permana"
    )

    private val locations = arrayOf(
            "Pittsburgh, PA, USA",
            "New Delhi, India",
            "California",
            "Sydney, Australia",
            "Trondheim, Norway",
            "India",
            "Kotagede, Yogyakarta, Indonesia",
            "Jakarta, Indonesia",
            "Bojongsoang - Bandung Jawa Barat",
            "Jakarta Indonesia"
    )

    private val repositories = arrayOf(
            "102",
            "37",
            "9",
            "30",
            "56",
            "28",
            "44",
            "110",
            "1064",
            "65"
    )

    private val companies = arrayOf(
            "Google, Inc.",
            "MindOrksOpenSource",
            "Google",
            "Google working on @android",
            "Working Group Two",
            "AndroidHive | Droid5",
            "gojek-engineering",
            "KotlinID",
            "JVMDeveloperID @KotlinID @IDDevOps",
            "Nusantara Beta Studio"
    )

    private val followers = arrayOf(
            "56995",
            "5153",
            "7972",
            "14725",
            "788",
            "18628",
            "277",
            "178",
            "428",
            "465"
    )

    private val followings = arrayOf(
            "12",
            "2",
            "0",
            "1",
            "0",
            "3",
            "39",
            "23",
            "61",
            "10"
    )

    private val avatars = arrayOf(
            "https://i.postimg.cc/XY63bvLL/user1.jpg",
            "https://i.postimg.cc/6Q0BzNd0/user2.jpg",
            "https://i.postimg.cc/nr6xMVJH/user3.jpg",
            "https://i.postimg.cc/j53t0Q3n/user4.jpg",
            "https://i.postimg.cc/vmMQWm6T/user5.jpg",
            "https://i.postimg.cc/hvJgSzNy/user6.jpg",
            "https://i.postimg.cc/3r6Yt1DP/user7.png",
            "https://i.postimg.cc/QdgDxrn3/user8.png",
            "https://i.postimg.cc/Z56bBNnX/user9.jpg",
            "https://i.postimg.cc/sXgsN0mR/user10.png"
    )

    val listUser: ArrayList<Users>
        get() {
            val list = arrayListOf<Users>()
            for (position in usernames.indices) {
                val users = Users()
                users.username = usernames[position]
                users.name = names[position]
                users.repository = repositories[position]
                users.company = companies[position]
                users.follower = followers[position]
                users.following = followings[position]
                users.avatar = avatars[position]
                users.location = locations[position]
                list.add(users)
            }
            return list
        }
}