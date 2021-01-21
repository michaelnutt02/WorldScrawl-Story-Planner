package com.example.wordscrawl

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class ProfileDetail(var type: TYPE = TYPE.SINGLE, var title: String = "", var body: String = "", var isSelected: Boolean = false) {
    enum class TYPE {
        SINGLE,
        PARAGRAPH,
        CATEGORY,
        TAGS
    }
    @get:Exclude var id: String = ""
    @ServerTimestamp var lastTouched: Timestamp? = null
    companion object {
        const val LAST_TOUCHED_KEY = "lastTouched"
        fun fromSnapshot(snapshot: DocumentSnapshot): ProfileDetail {
            val mq = snapshot.toObject(ProfileDetail::class.java)!!
            mq.id = snapshot.id
            return mq
        }
    }
}
