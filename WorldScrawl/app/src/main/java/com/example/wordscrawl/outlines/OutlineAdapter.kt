package com.example.wordscrawl.outlines

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.ProfileDetail
import com.example.wordscrawl.R
import com.example.wordscrawl.WorldsFragment
import com.example.wordscrawl.editprofile.EditProfileViewHolder
import com.example.wordscrawl.profilecategory.Profile
import com.google.firebase.firestore.*

class OutlineAdapter(var context: Context, val profile:Profile, var listener : WorldsFragment.OnProfileSelectedListener): RecyclerView.Adapter<OutlineViewHolder>() {
    private var outlines: ArrayList<ProfileDetail> = arrayListOf()



    private val detailsRef = FirebaseFirestore
        .getInstance()
        .collection("profile-details")


    init {

        //continously add, modify, delete outlines from a story
        detailsRef
            .orderBy(Profile.LAST_TOUCHED_KEY, Query.Direction.ASCENDING)
            .whereEqualTo("profileId", profile.id)
            .addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if(error != null){
                    Log.e("ERROR","Listen error $error")
                }
                for(docChange in snapshot!!.documentChanges){
                    val profileDetail = ProfileDetail.fromSnapshot(docChange.document)
                    Log.i("adding", "STORIES profile id is ${profile.id} in adapter")
                    when(docChange.type){
                        DocumentChange.Type.ADDED ->{
                            val pos = outlines.size
                            outlines.add(pos, profileDetail)
                            notifyItemInserted(pos)
                        }
                        DocumentChange.Type.REMOVED ->{
                            val pos = outlines.indexOfFirst{profileDetail.id == it.id}
                            outlines.removeAt(pos)
                            notifyItemRemoved(pos)
                        }
                        DocumentChange.Type.MODIFIED ->{
                            val pos = outlines.indexOfFirst{profileDetail.id == it.id}
                            outlines[pos] = profileDetail
                            notifyItemChanged(pos)
                        }
                    }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutlineViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.profile_card, parent, false)


        return OutlineViewHolder(view, this, context)
    }

    override fun onBindViewHolder(holder: OutlineViewHolder, position: Int) {

        holder.bind(outlines[position])
    }

    override fun getItemCount(): Int {
        return outlines.size
    }

    fun selectOutlineAt(position: Int){
        listener.onOutlineSelected(profile)
    }

    fun add(outline:ProfileDetail){
        Log.i("adding","STORIES, profile id is ${profile.id} in adapter")
        detailsRef.add(outline)
    }


}