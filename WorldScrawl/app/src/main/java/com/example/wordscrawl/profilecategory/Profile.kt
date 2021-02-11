package com.example.wordscrawl.profilecategory

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*

data class Profile(var type:String = "CHARACTER", var name: String = "", var tags: ArrayList<String> = arrayListOf(), var picture: Int? = null){


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
