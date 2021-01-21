package com.example.wordscrawl.profilecategory

import com.example.wordscrawl.ProfileDetail

data class Profile(var name: String, var details: ArrayList<ProfileDetail> = arrayListOf(), var picture: Int? = null)
