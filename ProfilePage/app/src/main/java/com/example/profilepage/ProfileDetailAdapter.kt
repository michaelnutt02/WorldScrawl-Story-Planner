package com.example.profilepage

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProfileDetailAdapter(var context: Context) : RecyclerView.Adapter<ProfileDetailViewHolder>() {
    private val profileDetails: ArrayList<ProfileDetail> = arrayListOf(
        ProfileDetail(ProfileDetail.TYPE.SINGLE, "Test Normal", "Value"),
        ProfileDetail(ProfileDetail.TYPE.SINGLE, "Test Normal2", "Value2"),
        ProfileDetail(ProfileDetail.TYPE.PARAGRAPH, "Test Paragraph", "blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah"),
        ProfileDetail(ProfileDetail.TYPE.CATEGORY, "Test Category"),
        ProfileDetail(ProfileDetail.TYPE.TAGS)
    )

    override fun onCreateViewHolder(parent: ViewGroup, index: Int): ProfileDetailViewHolder {
        Log.d("PD", "index is ${index}")
        val view = LayoutInflater.from(context).inflate(
            when(profileDetails[index].type){
                ProfileDetail.TYPE.SINGLE -> R.layout.profile_detail_single_card
                ProfileDetail.TYPE.PARAGRAPH -> R.layout.profile_detail_paragraph_card
                ProfileDetail.TYPE.CATEGORY -> R.layout.profile_detail_category_card
                ProfileDetail.TYPE.TAGS -> R.layout.profile_detail_tags_card
            }
            , parent, false)
        return ProfileDetailViewHolder(view, this, context)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(viewHolder: ProfileDetailViewHolder, index: Int) {
        viewHolder.bind(profileDetails[index])
    }

    override fun getItemCount() = profileDetails.size

    fun add(profileDetail: ProfileDetail) {
        profileDetails.add(profileDetail)
        notifyItemInserted(profileDetails.size-1)
    }

    fun remove(position: Int) {
        profileDetails.removeAt(position)
        notifyItemRemoved(position)
    }

    fun selectProfileDetail(position: Int) {
        profileDetails[position].isSelected = !profileDetails[position].isSelected
        notifyItemChanged(position)
    }
}