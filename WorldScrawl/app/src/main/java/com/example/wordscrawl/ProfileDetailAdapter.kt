package com.example.wordscrawl

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

class ProfileDetailAdapter(var context: Context, var profileId: String) : RecyclerView.Adapter<ProfileDetailViewHolder>() {
    private val profileDetails: ArrayList<ProfileDetail> = arrayListOf()

    init {
        val profileRef = FirebaseFirestore
            .getInstance()
            .collection("profiles")
            .document(profileId)

        profileRef.get().addOnSuccessListener { snapshot: DocumentSnapshot ->
            val detailRefs: ArrayList<DocumentReference> = snapshot["details"] as ArrayList<DocumentReference>
            for(doc in detailRefs) {
                doc.addSnapshotListener { snapshot: DocumentSnapshot?, exception: FirebaseFirestoreException? ->
                    if(exception != null) {
                        Log.e("MQ", "Listen error $exception")
                    }
                    if(!snapshot?.exists()!!) Log.d("WS",  "this detail doc (${snapshot.id}) doesn't exist")
                    val profileDetail = ProfileDetail.fromSnapshot(snapshot)
                    if (profileDetail != null) {
                        val pos = profileDetails.indexOfFirst { profileDetail.id == it.id }
                        Log.d("WS",  "pos is $pos")
                        when (pos) {
                            -1 -> profileDetails.add(profileDetails.size, profileDetail)
                            else -> profileDetails[pos] = profileDetail
                        }
                        notifyDataSetChanged()
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
        return ProfileDetailViewHolder(view, this, context)
    }

    fun showAddDialog(position: Int = -1) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.add_detail_title))
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add, null, false)
        builder.setView(view)
        builder.setPositiveButton(android.R.string.ok) {_,_->
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