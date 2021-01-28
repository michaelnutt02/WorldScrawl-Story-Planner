package com.example.wordscrawl

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class ProfileDetail(var type: TYPE = TYPE.SINGLE, var title: String = "", var body: String = "", var profileId: String = "", var isSelected: Boolean = false) {
    enum class TYPE { //remove isSelected, it breaks the selection in certain conditions
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
            val pd = snapshot.toObject(ProfileDetail::class.java)!!
            pd.id = snapshot.id
            return pd
        }
    }
}
