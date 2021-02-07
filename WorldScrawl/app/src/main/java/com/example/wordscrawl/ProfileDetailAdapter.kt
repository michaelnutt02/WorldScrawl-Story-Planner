package com.example.wordscrawl

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.profilecategory.Profile
import com.google.firebase.firestore.*

class ProfileDetailAdapter(var context: Context, var profile: Profile, var listener: WorldsFragment.OnProfileSelectedListener?) : RecyclerView.Adapter<ProfileDetailViewHolder>() {
    private val profileDetails: ArrayList<ProfileDetail> = arrayListOf()
    private val detailsRef = FirebaseFirestore
            .getInstance()
            .collection("profile-details")

    init {
        detailsRef
        .orderBy(Profile.LAST_TOUCHED_KEY, Query.Direction.ASCENDING)
        .whereEqualTo("profileId", profile.id)
//        .whereNotEqualTo("type", ProfileDetail.TYPE.TAGS.toString())
        .addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
            if(error != null){
                Log.e("ERROR","Listen error $error")
            }
            for(docChange in snapshot!!.documentChanges){
                val profileDetail = ProfileDetail.fromSnapshot(docChange.document)
                when(docChange.type){
                    DocumentChange.Type.ADDED ->{
                        val pos = profileDetails.size
                        profileDetails.add(pos, profileDetail)
                        notifyItemInserted(pos)
                    }
                    DocumentChange.Type.REMOVED ->{
                        val pos = profileDetails.indexOfFirst{profileDetail.id == it.id}
                        profileDetails.removeAt(pos)
                        notifyItemRemoved(pos)
                    }
                    DocumentChange.Type.MODIFIED ->{
                        val pos = profileDetails.indexOfFirst{profileDetail.id == it.id}
                        profileDetails[pos] = profileDetail
                        notifyItemChanged(pos)
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, itemType: Int): ProfileDetailViewHolder {
        val view = LayoutInflater.from(context).inflate(
            when(itemType){
                0 -> R.layout.profile_detail_single_card
                1 -> R.layout.profile_detail_paragraph_card
                2 -> R.layout.profile_detail_category_card
                else -> R.layout.profile_detail_tags_card
            }
            , parent, false)
        return ProfileDetailViewHolder(view, this, context, listener, profile)
    }

    fun showAddDialog(position: Int = -1) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.add_detail_title))
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add, null, false)
        val spinner: Spinner = view.findViewById(R.id.add_details_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                context,
                R.array.add_details_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        builder.setView(view)
        builder.setPositiveButton(android.R.string.ok) {_,_->
            add(ProfileDetail(type = when(spinner.selectedItem){
                "SINGLE" -> ProfileDetail.TYPE.SINGLE
                "PARAGRAPH" -> ProfileDetail.TYPE.PARAGRAPH
                "CATEGORY" -> ProfileDetail.TYPE.CATEGORY
                else -> ProfileDetail.TYPE.TAGS
            }, profileId = profile.id))
        }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.create().show()
    }

    override fun getItemViewType(position: Int): Int {
        return when(profileDetails[position].type) {
            ProfileDetail.TYPE.SINGLE -> 0
            ProfileDetail.TYPE.PARAGRAPH -> 1
            ProfileDetail.TYPE.CATEGORY -> 2
            else -> 3
        }
    }

    override fun onBindViewHolder(viewHolder: ProfileDetailViewHolder, index: Int) {
        viewHolder.bind(profileDetails[index])
    }

    override fun getItemCount() = profileDetails.size

    fun add(profileDetail: ProfileDetail) {
        detailsRef.add(profileDetail)
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