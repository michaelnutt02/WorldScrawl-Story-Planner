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

class ProfileCardAdapter(var context: Context, var listener: WorldsFragment.OnProfileSelectedListener?) : RecyclerView.Adapter<ProfileCardViewHolder>() {
    private val profiles: ArrayList<Profile> = arrayListOf(
        Profile("Bob"),
        Profile("Harry Potter", arrayListOf(), R.drawable.harry_potter)
    )

    init {
        //we will want to make a characters/world/story parameter later for collection path
        val profilesRef = FirebaseFirestore
                .getInstance()
                .collection("profiles")

//        profilesRef.get().addOnSuccessListener { snapshot: QuerySnapshot? ->
//            if (snapshot != null) {
//                for(doc in snapshot){
//                    val pr = doc.toObject(Profile::class.java)
//                    add(pr)
//                }
//            }
//        }


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

    fun add(profileDetail: Profile) {
        profiles.add(profileDetail)
        notifyItemInserted(profiles.size-1)
    }

    fun remove(position: Int) {
        profiles.removeAt(position)
        notifyItemRemoved(position)
    }

    fun selectProfileAt(adapterPosition: Int){
        val profile = profiles[adapterPosition]
        if(listener != null){
            listener?.onProfileSelected(profile)
        }

    }
}