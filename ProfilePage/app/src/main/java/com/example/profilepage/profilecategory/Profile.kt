package com.example.profilepage.profilecategory

import com.example.profilepage.ProfileDetail

data class Profile(var name: String, var details: ArrayList<ProfileDetail> = arrayListOf(), var picture: Int? = null)
