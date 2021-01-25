package com.example.wordscrawl.profilecategory

import com.example.wordscrawl.ProfileDetail
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class Profile(var name: String = "", var details: ArrayList<ProfileDetail> = arrayListOf(), var picture: Int? = null){

    @get:Exclude
    var id: String = ""
    @ServerTimestamp
    var lastTouched: Timestamp? = null
    companion object {
        const val LAST_TOUCHED_KEY = "lastTouched"
        fun fromSnapshot(snapshot: DocumentSnapshot): Profile {
            val profile = snapshot.toObject(Profile::class.java)!!
            profile.id = snapshot.id
            return profile
        }
    }
}
