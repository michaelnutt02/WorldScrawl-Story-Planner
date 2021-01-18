package com.example.profilepage.profilecategory

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.profilepage.R

class ProfileCardAdapter(var context: Context) : RecyclerView.Adapter<ProfileCardViewHolder>() {
    private val profiles: ArrayList<Profile> = arrayListOf(
        Profile("Bob"),
        Profile("Harry Potter", arrayListOf(), R.drawable.harry_potter)
    )

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
}