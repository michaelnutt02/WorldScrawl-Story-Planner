package com.example.wordscrawl

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.profilecategory.Profile
import com.example.wordscrawl.profilecategory.ProfileCardAdapter
import com.example.wordscrawl.profiletag.ProfileTagAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfileDetailViewHolder : RecyclerView.ViewHolder {
    lateinit var context: Context
    var listener: WorldsFragment.OnProfileSelectedListener?
    lateinit var profile: Profile

    constructor(itemView: View, adapter: ProfileDetailAdapter, context: Context, listener: WorldsFragment.OnProfileSelectedListener?, profile: Profile): super(itemView) {
        this.context = context
        this.listener = listener
        this.profile = profile
        itemView.setOnClickListener {
            adapter.selectProfileDetail(adapterPosition)
        }
    }

    fun bind(profileDetail: ProfileDetail) {
//        val existingCardView = itemView.findViewById<CardView>(R.id.profile_detail_single_view)
//        if(existingCardView != null) itemView.findViewById<RecyclerView>(R.id.profile_detail_recycler).remove(existingCardView)
        var color = ContextCompat.getColor(context, R.color.grey)
        val cardView: CardView = when (profileDetail.type) {
            ProfileDetail.TYPE.SINGLE -> itemView.findViewById<CardView>(R.id.profile_detail_single_view)
            ProfileDetail.TYPE.PARAGRAPH -> itemView.findViewById<CardView>(R.id.profile_detail_paragraph_view)
            ProfileDetail.TYPE.CATEGORY -> itemView.findViewById<CardView>(R.id.profile_detail_category_view)
            ProfileDetail.TYPE.TAGS -> itemView.findViewById<CardView>(R.id.profile_detail_tags_view)
            else -> itemView.findViewById<CardView>(R.id.profile_detail_tags_view)
        }

        if(profileDetail.type != ProfileDetail.TYPE.TAGS) {
            val detailTitle: TextView = itemView.findViewById(R.id.profile_detail_title)
            detailTitle.text = profileDetail.title
        }
        if(profileDetail.type == ProfileDetail.TYPE.SINGLE) {
            val detailBody: TextView = itemView.findViewById(R.id.profile_detail_body)
            detailBody.text = ": ${profileDetail.body}"
        }

        if(profileDetail.type == ProfileDetail.TYPE.PARAGRAPH) {
            if(profileDetail.isSelected) {
                val verticalView: LinearLayout = itemView.findViewById(R.id.paragraph_vertical_view)
                val bodyView: View? = itemView.findViewById(R.id.paragraph_detail_body_view)
                if(bodyView != null) {
                    verticalView.removeView(bodyView)
                    verticalView.addView(bodyView)
                }
                else LayoutInflater.from(context).inflate(R.layout.paragraph_body, verticalView)
                val detailBody: TextView = itemView.findViewById(R.id.profile_detail_body)
                detailBody.text = profileDetail.body
                color = ContextCompat.getColor(context, R.color.white)
            } else {

            }
        }

        if(profileDetail.type == ProfileDetail.TYPE.CATEGORY) {
            val adapter = ProfileCardAdapter(context, listener, "CHARACTER")
            val profileCategoryRecycler = cardView.findViewById<RecyclerView>(R.id.profile_category_recycler)
            profileCategoryRecycler.layoutManager = LinearLayoutManager(context)
            profileCategoryRecycler.adapter = adapter
        }

        if(profileDetail.type == ProfileDetail.TYPE.TAGS) {
            val adapter = ProfileTagAdapter(context, listener, profile)
            val profileCategoryRecycler = cardView.findViewById<RecyclerView>(R.id.profile_tags_recycler)
            profileCategoryRecycler.layoutManager = LinearLayoutManager(context)
            profileCategoryRecycler.adapter = adapter
        }

        cardView.setCardBackgroundColor(color)
    }
}