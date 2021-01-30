package com.example.wordscrawl

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ServerTimestamp

data class ProfileDetail(var type: TYPE = TYPE.SINGLE, var title: String = "", var body: String = "", var profileId: String = "", var isSelected: Boolean = false) {
    enum class TYPE { //remove isSelected, it breaks the selection in certain conditions
        SINGLE,
        PARAGRAPH,
        CATEGORY,
        TAGS
    }

    fun setDetailBody(newBody:String){
        body = newBody

        //update the type of detail every time the body is set
        if(body.count() > 50){
            type = ProfileDetail.TYPE.PARAGRAPH
        }else{
            type = ProfileDetail.TYPE.SINGLE
        }
    }
    fun setDetailTitle(newTitle:String){
        title = newTitle
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

        fun fromSnapshots(snapshot: QuerySnapshot):ArrayList<ProfileDetail>{
            var list = arrayListOf<ProfileDetail>()

            val pds = snapshot.toObjects(ProfileDetail::class.java)
            if(pds != null){
                var i = 0
                for(pd in pds){
                    pd.id = snapshot.elementAt(i).id
                    i++
                    list.add(pd)
                }
            }


            return list
        }
    }
}
