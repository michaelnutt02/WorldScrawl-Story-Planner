package com.example.wordscrawl

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
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
            val sample: DocumentReference? = null

            for(profileDetail in detailRefs) {
                profileDetail.get().addOnSuccessListener { snapshot: DocumentSnapshot ->
                    if(!snapshot.exists()) Log.d("WS",  "this detail doc (${snapshot.id}) doesn't exist")
                    val newProfileDetail = snapshot.toObject(ProfileDetail::class.java)
                    if (newProfileDetail != null) {
                        profileDetails.add(newProfileDetail)
                        notifyDataSetChanged()
                    }
                }
            }
        }


//        quotesRef.orderBy(ProfileDetail.LAST_TOUCHED_KEY, Query.Direction.ASCENDING)
//            .addSnapshotListener { snapshot: QuerySnapshot?, exception: FirebaseFirestoreException? ->
//                if(exception != null) {
//                    Log.e("MQ", "Listen error $exception")
//                }
//                for(docChange in snapshot!!.documentChanges) {
//                    val profileDetail = ProfileDetail.fromSnapshot(docChange.document)
//                    when (docChange.type) {
//                        DocumentChange.Type.ADDED -> {
//                            profileDetails.add(0, profileDetail)
//                            notifyItemInserted(0)
//                        }
//                        DocumentChange.Type.REMOVED -> {
//                            val pos = profileDetails.indexOfFirst { profileDetail.id == it.id }
//                            profileDetails.removeAt(pos)
//                            notifyItemRemoved(pos)
//                        }
//                        DocumentChange.Type.MODIFIED -> {
//                            val pos = profileDetails.indexOfFirst { profileDetail.id == it.id }
//                            profileDetails[pos] = profileDetail
//                            notifyItemChanged(pos)
//                        }
//                    }
//                }
//            }
    }

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