package com.example.wordscrawl.outlines


import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ServerTimestamp

data class Outline(var type: TYPE = TYPE.FREEFORM, var title: String = "", var body: String = "", var profileId: String = "", var isSelected: Boolean = false) {
    enum class TYPE { //remove isSelected, it breaks the selection in certain conditions
        FREEFORM,
        PINCH,
        FREYTAG
    }

    fun setDetailBody(newBody:String){
        body = newBody
    }
    fun setDetailTitle(newTitle:String){
        title = newTitle
    }


    @get:Exclude var id: String = ""
    @ServerTimestamp var lastTouched: Timestamp? = null
    companion object {
        const val LAST_TOUCHED_KEY = "lastTouched"
        fun fromSnapshot(snapshot: DocumentSnapshot): Outline {
            val pd = snapshot.toObject(Outline::class.java)!!
            pd.id = snapshot.id
            return pd
        }

        fun fromSnapshots(snapshot: QuerySnapshot):ArrayList<Outline>{
            var list = arrayListOf<Outline>()

            val pds = snapshot.toObjects(Outline::class.java)
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
