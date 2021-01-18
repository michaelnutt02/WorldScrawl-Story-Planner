package com.example.profilepage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.profilepage.profilecategory.ProfileCardAdapter

class ProfileDetailViewHolder : RecyclerView.ViewHolder {
    lateinit var context: Context

    constructor(itemView: View, adapter: ProfileDetailAdapter, context: Context): super(itemView) {
        this.context = context
        itemView.setOnClickListener {
            adapter.selectProfileDetail(adapterPosition)
        }
    }

    fun bind(profileDetail: ProfileDetail) {
        var color = ContextCompat.getColor(context, R.color.grey)
        val cardView: CardView = when (profileDetail.type) {
            ProfileDetail.TYPE.SINGLE -> itemView.findViewById<CardView>(R.id.profile_detail_single_view)
            ProfileDetail.TYPE.PARAGRAPH -> itemView.findViewById<CardView>(R.id.profile_detail_paragraph_view)
            ProfileDetail.TYPE.CATEGORY -> itemView.findViewById<CardView>(R.id.profile_detail_category_view)
            ProfileDetail.TYPE.TAGS -> itemView.findViewById<CardView>(R.id.profile_detail_tags_view)
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
            val adapter = ProfileCardAdapter(context)
            val profileCategoryRecycler = cardView.findViewById<RecyclerView>(R.id.profile_category_recycler)
            profileCategoryRecycler.layoutManager = LinearLayoutManager(context)
            profileCategoryRecycler.setHasFixedSize(true)
            profileCategoryRecycler.adapter = adapter
        }

        cardView.setCardBackgroundColor(color)
    }
}