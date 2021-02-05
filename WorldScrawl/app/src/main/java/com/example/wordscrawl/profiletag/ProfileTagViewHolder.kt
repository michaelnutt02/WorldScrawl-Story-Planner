package com.example.wordscrawl.profiletag

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.R
import com.example.wordscrawl.profilecategory.Profile

class ProfileTagViewHolder : RecyclerView.ViewHolder {
    lateinit var context: Context

    constructor(itemView: View, adapter: ProfileTagAdapter, context: Context): super(itemView) {
        this.context = context

        itemView.setOnClickListener{
            adapter.selectProfileAt(adapterPosition)
        }

        //TODO: Make a copy function where writer can long click on a profile and copy its fields (bodies not filled) into a new profile
        itemView.setOnLongClickListener{
            adapter.remove(adapterPosition)
            true
        }
    }

    fun bind(profile: Profile) {
        if(profile.picture == null){
            Log.i("adding profile", "${profile.name} has null picture" )
        }else{
            Log.i("adding profile", "${profile.name} does not have null picture" )
        }

        var color = ContextCompat.getColor(context, R.color.light_blue_600)
        val cardView: CardView = when (profile.picture) {
            null -> itemView.findViewById<CardView>(R.id.tag_card_view)
            -1 -> itemView.findViewById<CardView>(R.id.tag_card_view)
            else -> itemView.findViewById<CardView>(R.id.tag_card_view)
        }

        itemView.findViewById<TextView>(R.id.tag_card_title).text = profile.name

        cardView.setCardBackgroundColor(color)
    }
}