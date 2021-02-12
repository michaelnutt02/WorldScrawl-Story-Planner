package com.example.wordscrawl.profilecategory

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.R
import com.squareup.picasso.Picasso

class ProfileCardViewHolder : RecyclerView.ViewHolder {
    lateinit var context: Context

    constructor(itemView: View, adapter: ProfileCardAdapter, context: Context): super(itemView) {
        this.context = context

        itemView.setOnClickListener{
            adapter.selectProfileAt(adapterPosition)
        }

        //TODO: Make a copy function where writer can long click on a profile and copy its fields (bodies not filled) into a new profile
//        itemView.setOnLongClickListener{
//            adapter.remove(adapterPosition)
//            true
//        }
    }

    fun bind(profile: Profile) {
        if(profile.picture == null){
            Log.i("adding profile", "${profile.name} has null picture" )
        }else{
            Log.i("adding profile", "${profile.name} does not have null picture" )
        }

        var color = ContextCompat.getColor(context, R.color.purple_200)
        val cardView: CardView = when (profile.picture) {
            "" -> itemView.findViewById<CardView>(R.id.profile_card_view)
            else -> itemView.findViewById<CardView>(R.id.profile_card_with_picture_view)
        }

        if(profile.picture.isNotEmpty()) {
            Picasso.get()
                .load(profile.picture)
                .into(itemView.findViewById<ImageView>(R.id.profile_card_picture))
        }

        itemView.findViewById<TextView>(R.id.profile_card_title).text = profile.name

        cardView.setCardBackgroundColor(color)
    }
}