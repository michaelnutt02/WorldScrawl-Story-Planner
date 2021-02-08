package com.example.wordscrawl.editprofile

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.ProfileDetail
import com.example.wordscrawl.ProfileDetailAdapter
import com.example.wordscrawl.R
import com.example.wordscrawl.WorldsFragment
import com.example.wordscrawl.profilecategory.Profile
import com.example.wordscrawl.profilecategory.ProfileCardAdapter
import com.example.wordscrawl.profiletag.ProfileTagAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class EditProfileViewHolder: RecyclerView.ViewHolder {

    lateinit var context: Context
    var listener: WorldsFragment.OnProfileSelectedListener?
    lateinit var profile: Profile
    lateinit var adapter: EditProfileAdapter

    constructor(itemView: View, adapter: EditProfileAdapter, context: Context, listener: WorldsFragment.OnProfileSelectedListener?, profile: Profile): super(itemView) {
        this.context = context
        this.listener = listener
        this.profile = profile
        this.adapter = adapter
    }

    fun bind(profileDetail: ProfileDetail) {
//        val existingCardView = itemView.findViewById<CardView>(R.id.profile_detail_single_view)
//        if(existingCardView != null) itemView.findViewById<RecyclerView>(R.id.profile_detail_recycler).remove(existingCardView)
        var color = ContextCompat.getColor(context, R.color.grey)
        val cardView: CardView = when (profileDetail.type) {
            ProfileDetail.TYPE.SINGLE -> itemView.findViewById<CardView>(R.id.profile_edit_text_card_view)
            ProfileDetail.TYPE.PARAGRAPH -> itemView.findViewById<CardView>(R.id.profile_edit_text_card_view)
            ProfileDetail.TYPE.CATEGORY -> itemView.findViewById<CardView>(R.id.profile_edit_category_card_view)
            ProfileDetail.TYPE.TAGS -> itemView.findViewById<CardView>(R.id.profile_edit_tags_card_view)
            else -> itemView.findViewById<CardView>(R.id.profile_edit_text_card_view)
        }

        if(profileDetail.type != ProfileDetail.TYPE.TAGS) {
            var titleEdit:EditText = itemView.findViewById(R.id.profile_edit_detail_title)
            titleEdit.setText(profileDetail.title)
            titleEdit.doAfterTextChanged {
                adapter.get(adapterPosition).setDetailTitle(titleEdit.text.toString())
            }
        }
        if(profileDetail.type == ProfileDetail.TYPE.SINGLE) {
            var bodyEdit:TextInputEditText = itemView.findViewById(R.id.profile_edit_detail_body)
            bodyEdit.setText(profileDetail.body)
            bodyEdit.doAfterTextChanged {
                adapter.get(adapterPosition).setDetailBody(bodyEdit.text.toString())
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

            cardView.findViewById<ImageButton>(R.id.add_tag_button).setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle(context.getString(R.string.add_tag_title))
                val view = LayoutInflater.from(context).inflate(R.layout.dialog_tag_add, null, false)
                val adapter = ProfileTagAdapter(context, listener, null)
                val profileCategoryRecycler = view.findViewById<RecyclerView>(R.id.add_tags_recycler)
                profileCategoryRecycler.layoutManager = LinearLayoutManager(context)
                profileCategoryRecycler.adapter = adapter

                builder.setView(view)
                builder.setNegativeButton(android.R.string.cancel, null)
                builder.create().show()
            }
        }

        cardView.setCardBackgroundColor(color)
    }
}