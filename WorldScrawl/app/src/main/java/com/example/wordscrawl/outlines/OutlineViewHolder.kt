package com.example.wordscrawl.outlines

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.ProfileDetail
import com.example.wordscrawl.R
import com.example.wordscrawl.editprofile.EditProfileAdapter

class OutlineViewHolder: RecyclerView.ViewHolder {

    lateinit var context: Context
    lateinit var adapter:OutlineAdapter

    constructor(itemView: View, adapter: OutlineAdapter, context: Context): super(itemView) {
        this.context = context
        this.adapter = adapter

        itemView.setOnClickListener{
            adapter.selectOutlineAt(adapterPosition)
        }

    }

    fun bind(outline: ProfileDetail) {
        var color = ContextCompat.getColor(context, R.color.purple_200)

        val cardView: CardView = itemView.findViewById<CardView>(R.id.profile_card_view)
        itemView.findViewById<TextView>(R.id.profile_card_title).text = outline.title

        cardView.setCardBackgroundColor(color)

    }
}