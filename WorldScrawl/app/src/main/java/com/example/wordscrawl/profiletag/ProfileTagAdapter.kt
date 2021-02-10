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
    private var profile: Profile? = profile

    private val profilesRef = FirebaseFirestore
            .getInstance()
            .collection("profiles")

    private lateinit var tagsListener: ListenerRegistration

    init {
        //we will want to make a characters/world/story parameter later for collection path
        initListener()
    }

    private fun initListener() {
        if(profile != null && profile!!.tags.size != 0) tagsListener = profilesRef
            .whereIn("__name__", profile!!.tags)
            .addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                handleSnapshot(snapshot, error)
            }
        else if(profile == null) profilesRef
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


    fun remove(position: Int) {
        if(profile != null) {
            profile!!.tags?.remove(profiles[position].id)
            profiles[position].tags.remove(profile!!.id)
            profilesRef.document(profiles[position].id).set(profiles[position])
            profiles.removeAt(position)
            tagsListener.remove()
            initListener()
            notifyItemRemoved(position)
        }

    }



    fun selectProfileAt(adapterPosition: Int){
        val profile = profiles[adapterPosition]
        if(listener != null){
            listener?.onProfileSelected(profile)
        }

    }
}