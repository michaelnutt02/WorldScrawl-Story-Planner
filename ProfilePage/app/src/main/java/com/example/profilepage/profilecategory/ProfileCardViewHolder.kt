package com.example.profilepage.profilecategory

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.profilepage.R

class ProfileCardViewHolder : RecyclerView.ViewHolder {
    lateinit var context: Context

    constructor(itemView: View, adapter: ProfileCardAdapter, context: Context): super(itemView) {
        this.context = context
    }

    fun bind(profile: Profile) {
        var color = ContextCompat.getColor(context, R.color.purple_200)
        val cardView: CardView = when (profile.picture) {
            null -> itemView.findViewById<CardView>(R.id.profile_card_view)
            else -> itemView.findViewById<CardView>(R.id.profile_card_with_picture_view)
        }

        if(profile.picture != null) {
            itemView.findViewById<ImageView>(R.id.profile_card_picture).setImageResource(
                profile.picture!!
            )
        }

        itemView.findViewById<TextView>(R.id.profile_card_title).text = profile.name

        cardView.setCardBackgroundColor(color)
    }
}