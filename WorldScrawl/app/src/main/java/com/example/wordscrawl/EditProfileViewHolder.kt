package com.example.wordscrawl

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.profilecategory.Profile
import com.example.wordscrawl.profilecategory.ProfileCardAdapter

class EditProfileViewHolder: RecyclerView.ViewHolder {

    lateinit var context:Context

    constructor(itemView: View, adapter: EditProfileAdapter, context: Context): super(itemView) {
        this.context = context
//        itemView.setOnClickListener {
//            adapter.selectProfileDetail(adapterPosition)
//        }

        var trashButton = itemView.findViewById<ImageButton>(R.id.trashButton)
        trashButton.setOnClickListener{
            adapter.remove(adapterPosition)
            //TODO: Make an undo snackbar for a card, also do it for profile swipe delete
        }
    }

    fun bind(editDetail: EditProfileDetail) {

    }

}