package com.example.wordscrawl

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class EditProfileAdapter(var context: Context): RecyclerView.Adapter<EditProfileViewHolder>() {
    private var editDetails: ArrayList<EditProfileDetail> = arrayListOf()

    init {
        if(this.itemCount == 0){
            var emptyEdit = EditProfileDetail("Empty")
            editDetails.add(emptyEdit)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditProfileViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.profile_edit_detail_card, parent, false)

        return EditProfileViewHolder(view, this, context)
    }

    override fun onBindViewHolder(holder: EditProfileViewHolder, position: Int) {
        holder.bind(editDetails[position])
    }

    override fun getItemCount(): Int {
        return editDetails.size
    }

    fun add(){
        var emptyEdit = EditProfileDetail("Empty")
        editDetails.add(0,emptyEdit)
        notifyItemInserted(0)
    }

    fun remove(position: Int){
        editDetails.removeAt(position)
        notifyItemRemoved(position)
    }
}