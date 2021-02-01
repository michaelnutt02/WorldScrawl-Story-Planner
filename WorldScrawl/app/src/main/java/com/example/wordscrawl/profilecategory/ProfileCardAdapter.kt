package com.example.wordscrawl.profilecategory

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.ProfileDetail
import com.example.wordscrawl.R
import com.example.wordscrawl.WorldsFragment
import com.google.firebase.firestore.*

class ProfileCardAdapter(var context: Context, var listener: WorldsFragment.OnProfileSelectedListener?, val type:Profile.TYPE) : RecyclerView.Adapter<ProfileCardViewHolder>() {
    private val profiles: ArrayList<Profile> = arrayListOf(
//        Profile(Profile.TYPE.CHARACTER,"Bob"),
//        Profile(Profile.TYPE.CHARACTER,"Harry Potter", arrayListOf(), R.drawable.harry_potter)
    )

    val profilesRef = FirebaseFirestore
            .getInstance()
            .collection("profiles")

    private val detailsRef = FirebaseFirestore
            .getInstance()
            .collection("profile-details")

    init {
        //we will want to make a characters/world/story parameter later for collection path
        profilesRef
                .orderBy("name",Query.Direction.DESCENDING)
                .whereEqualTo("type",type.toString())
                .addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
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



    }

    override fun onCreateViewHolder(parent: ViewGroup, index: Int): ProfileCardViewHolder {
        Log.d("PD", "index is ${index}")
        val view = LayoutInflater.from(context).inflate(
            when(profiles[index].picture){
                null -> R.layout.profile_card
                else -> R.layout.profile_card_with_picture
            }
            , parent, false)
        return ProfileCardViewHolder(view, this, context)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(viewHolder: ProfileCardViewHolder, index: Int) {
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