package com.example.wordscrawl.editprofile

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.ProfileDetail
import com.example.wordscrawl.ProfileDetailViewHolder
import com.example.wordscrawl.R
import com.example.wordscrawl.WorldsFragment
import com.example.wordscrawl.profilecategory.Profile
import com.google.firebase.firestore.*

class EditProfileAdapter(var context: Context, var profile: Profile, var listener : WorldsFragment.OnProfileSelectedListener): RecyclerView.Adapter<EditProfileViewHolder>() {
    private var editDetails: ArrayList<ProfileDetail> = arrayListOf()

    private var deletedDetails: ArrayList<ProfileDetail> = arrayListOf()

    private val detailsRef = FirebaseFirestore
            .getInstance()
            .collection("profile-details")

    private val profilesRef = FirebaseFirestore
            .getInstance()
            .collection("profiles")

    lateinit var viewHolder: EditProfileViewHolder

//    private var listener : WorldsFragment.OnProfileSelectedListener = context as WorldsFragment.OnProfileSelectedListener


    init {

        var isInitialized = false
        //add all existing details of the profile into editDetails
        detailsRef
        .orderBy(Profile.LAST_TOUCHED_KEY, Query.Direction.ASCENDING)
        .whereEqualTo("profileId",profile.id)
        .addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
            if(error != null){
                Log.e("ERROR","Listen error $error")
            }
            if(snapshot != null && !isInitialized){
                Log.i("adding", "Has details, profile id is ${profile.id}")
                editDetails.addAll(ProfileDetail.fromSnapshots(snapshot))
                notifyDataSetChanged()
            }else{
                var emptyEdit = ProfileDetail()
                editDetails.add(0,emptyEdit)
                notifyItemInserted(0)
                Log.i("adding", "No details, profile id is ${profile.id}")
            }
            isInitialized = true

            Log.i("adding", "In loop, profile id is ${profile.id}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, itemType: Int): EditProfileViewHolder {

        val view = LayoutInflater.from(context).inflate(
                when(itemType){
                    0 -> R.layout.profile_edit_text_card
                    1 -> R.layout.profile_edit_text_card
                    2 -> R.layout.profile_edit_category_card
                    else -> R.layout.profile_edit_tags_card
                }
                , parent, false)

        return EditProfileViewHolder(view, this, context, listener, profile)
    }

    override fun getItemViewType(position: Int): Int {
        return when(editDetails[position].type) {
            ProfileDetail.TYPE.SINGLE -> 0
            ProfileDetail.TYPE.PARAGRAPH -> 1
            ProfileDetail.TYPE.CATEGORY -> 2
            else -> 3
        }
    }

    override fun onBindViewHolder(viewHolder: EditProfileViewHolder, index: Int) {
        viewHolder.bind(editDetails[index])
    }

    override fun getItemCount(): Int {
        return editDetails.size
    }

    fun add(){
        var emptyEdit = ProfileDetail()
        editDetails.add(0,emptyEdit)
        notifyItemInserted(0)
    }

    fun remove(position: Int){
        //add to delete list if it was in firestore
        if(!editDetails[position].profileId.equals("")){
            deletedDetails.add(editDetails[position])
        }

        editDetails.removeAt(position)
        notifyItemRemoved(position)
    }

    fun get(position: Int): ProfileDetail {
        return editDetails[position]
    }


    fun updateFirestore(){


        //delete all details
        for(detail in deletedDetails){
            detailsRef.document(detail.id).delete()
        }
        Log.i("arrayList is","${editDetails.toString()}")

        //add and modify the rest of the details (make sure to convert to profileDetail)
        for(detail in editDetails){


            if(detail.profileId.equals("")){
                detail.profileId = profile.id

                detailsRef.add(detail)
                Log.i("profileId","adding ${detail.title} to firestore")
            }else{

                detailsRef.document(detail.id).set(detail)
                Log.i("profileId","modifying ${detail.title} in firestore")
            }

        }

    }

    fun addTag(tag:Profile){
        profile.tags.add(tag.id)
        Log.i("adding", "IN EDIT PROFILE, CHANGED PROFILE, NEEDS FIRESTORE UPDATED")
    }


}