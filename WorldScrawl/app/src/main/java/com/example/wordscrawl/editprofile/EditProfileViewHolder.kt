package com.example.wordscrawl.editprofile

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.ProfileDetail
import com.example.wordscrawl.R
import com.google.android.material.textfield.TextInputEditText

class EditProfileViewHolder: RecyclerView.ViewHolder {

    lateinit var context:Context
    lateinit var adapter: EditProfileAdapter

    var titleEdit:EditText = itemView.findViewById(R.id.profile_edit_detail_title)
    var bodyEdit:TextInputEditText = itemView.findViewById(R.id.profile_edit_detail_body)


    constructor(itemView: View, adapter: EditProfileAdapter, context: Context): super(itemView) {
        this.context = context
        this.adapter = adapter


        var trashButton = itemView.findViewById<ImageButton>(R.id.trashButton)
        trashButton.setOnClickListener{
            adapter.remove(adapterPosition)
            //TODO: Make an undo snackbar for a card, also do it for profile swipe delete
        }

        //Change details if the text is edited
        titleEdit.doAfterTextChanged {
            adapter.get(adapterPosition).setDetailTitle(titleEdit.text.toString())
        }
        bodyEdit.doAfterTextChanged {
            adapter.get(adapterPosition).setDetailBody(bodyEdit.text.toString())
        }


    }

    fun bind(editDetail: ProfileDetail) {


        Log.i("profileId", " body is ${bodyEdit.text.toString()}")
    }



}