package com.example.wordscrawl.profiletag

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.ProfileDetail
import com.example.wordscrawl.R
import com.example.wordscrawl.WorldsFragment
import com.example.wordscrawl.profilecategory.*
import com.google.firebase.firestore.*

class ProfileTagAdapter(var context: Context, var listener: WorldsFragment.OnProfileSelectedListener?, profile:Profile?) : RecyclerView.Adapter<ProfileTagViewHolder>() {
    private val profiles: ArrayList<Profile> = arrayListOf()

    private val profilesRef = FirebaseFirestore
            .getInstance()
            .collection("profiles")

    private val detailsRef = FirebaseFirestore
            .getInstance()
            .collection("profile-details")

    init {
        //we will want to make a characters/world/story parameter later for collection path
        if(profile != null) profilesRef
            .whereIn("__name__", profile.tags)
            .addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                handleSnapshot(snapshot, error)
            }
        else profilesRef
            .addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                handleSnapshot(snapshot, error)
            }
    }

    private fun handleSnapshot(snapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if(error != null){
            Log.e("ERROR","Listen error $error")
        }
        for(docChange in snapshot!!.documentChanges){
            val profile = Profile.fromSnapshot(docChange.document)
            when(docChange.type){
                DocumentChange.Type.ADDED ->{
                    profiles.add(0,profile)
                    notifyItemInserted(0)
                    Log.i("adding profile","${profile.name} in adapter with type ${profile.type}")
                }
                DocumentChange.Type.REMOVED ->{
                    val pos = profiles.indexOfFirst{profile.id == it.id}
                    profiles.removeAt(pos)
                    notifyItemRemoved(pos)
                }
                DocumentChange.Type.MODIFIED ->{
                    val pos = profiles.indexOfFirst{profile.id == it.id}
                    profiles[pos] = profile
                    notifyItemChanged(pos)

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, index: Int): ProfileTagViewHolder {
        Log.d("PD", "index is ${index}")
        val view = LayoutInflater.from(context).inflate(
            when(profiles[index].picture){
                null -> R.layout.tag_card
                else -> R.layout.tag_card
            }
            , parent, false)


        return ProfileTagViewHolder(view, this, context)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(viewHolder: ProfileTagViewHolder, index: Int) {
        viewHolder.bind(profiles[index])
    }

    override fun getItemCount() = profiles.size

    fun add(profile: Profile) {

        profilesRef.add(profile)

    }


    fun remove(position: Int) {

        //DONE: we need to also delete all details that belong to that profile in firestore
        var details:ArrayList<ProfileDetail> = arrayListOf()
        detailsRef
                .whereEqualTo("profileId",profiles[position].id)
                .addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->

                    if(error != null){
                        Log.e("ERROR","Listen error $error")
                    }

                    if(snapshot != null){
                        for(doc in snapshot){
                            val detail = ProfileDetail.fromSnapshot(doc)
                            details.add(detail)
                            Log.i("adding", "snapshot is not empty, details is size ${details.size} and detail id is ${detail.id}")
                        }
                        for(detail in details){
                            detailsRef.document(detail.id).delete()
                            Log.i("adding", "deleting detail with id ${detail.id}")
                        }

                    }else{
                        Log.i("adding", "snapshot is empty")
                    }
                }



        profilesRef.document(profiles[position].id).delete()
    }



    fun selectProfileAt(adapterPosition: Int){
        val profile = profiles[adapterPosition]
        if(listener != null){
            listener?.onProfileSelected(profile)
        }

    }
}