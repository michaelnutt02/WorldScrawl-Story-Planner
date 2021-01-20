package com.example.worldscrawl.profilecategory

import com.example.worldscrawl.ProfileDetail

data class Profile(var name: String, var details: ArrayList<ProfileDetail> = arrayListOf(), var picture: Int? = null)
