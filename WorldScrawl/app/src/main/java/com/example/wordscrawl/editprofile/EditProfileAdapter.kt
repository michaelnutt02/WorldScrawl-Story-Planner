package com.example.wordscrawl.editprofile

import android.app.PendingIntent.getActivity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.*
import com.example.wordscrawl.R
import com.example.wordscrawl.profilecategory.Profile
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import kotlin.random.Random

class EditProfileAdapter(var context: Context, var profile: Profile, var listener : WorldsFragment.OnProfileSelectedListener, var editFragment: EditProfileFragment): RecyclerView.Adapter<EditProfileViewHolder>() {
    private var editDetails: ArrayList<ProfileDetail> = arrayListOf(ProfileDetail(ProfileDetail.TYPE.TAGS))
    private val tagsOffset = 1

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

        //add all existing details of the profile into editDetails
        detailsRef
                .orderBy(Profile.LAST_TOUCHED_KEY, Query.Direction.ASCENDING)
                .whereEqualTo("profileId",profile.id)
                .get()
                .addOnSuccessListener{ snapshot: QuerySnapshot? ->

                    if(snapshot != null){
                        Log.i("adding", "Has details, profile id is ${profile.id}")
                        for(editDetail in ProfileDetail.fromSnapshots(snapshot))
                        {
                            val pos = editDetails.size - tagsOffset
                            editDetails.add(pos, editDetail)
                            notifyItemInserted(pos)
                        }
                    }else{
//                        var emptyEdit = ProfileDetail()
//                        editDetails.add(0,emptyEdit)
//                        notifyItemInserted(0)
                        Log.i("adding", "No details, profile id is ${profile.id}")
                    }

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

    fun add() {
        when(profile.type) {
            "CHARACTER" -> add(ProfileDetail(ProfileDetail.TYPE.SINGLE))
            "WORLD" -> add(ProfileDetail(ProfileDetail.TYPE.CATEGORY))
            else -> add(ProfileDetail(ProfileDetail.TYPE.SINGLE))
        }
    }

    fun addCategory(profileDetail: ProfileDetail){
        detailsRef.add(profileDetail)
        var newDetail:ProfileDetail? = null
        //get latest added detail so we can have detail id
        detailsRef.orderBy(ProfileDetail.LAST_TOUCHED_KEY, Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener {
            Log.i("adding", "we are getting here :)))")

            for(doc in it){
                newDetail = ProfileDetail.fromSnapshot(doc)
                }
            }

        newDetail?.let { add(it) }
    }

    fun add(profileDetail: ProfileDetail) {
        val pos = editDetails.size - tagsOffset
        editDetails.add(pos, profileDetail)
        notifyItemInserted(pos)
        editFragment.scrollToPosition(pos)
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
            if(detail.type != ProfileDetail.TYPE.TAGS) detailsRef.document(detail.id).delete()
        }
        Log.i("arrayList is","${editDetails.toString()}")

        //add and modify the rest of the details (make sure to convert to profileDetail)
        for(detail in editDetails){
            if(detail.type != ProfileDetail.TYPE.TAGS){
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

    }

    fun addTag(tag:Profile){
        profile.tags.add(tag.id)
//        profilesRef.document(tag.id).addSnapshotListener{snapshot, _ ->
//            val addedProfile = Profile.fromSnapshot(snapshot!!)
//            addedProfile.tags.add(profile.id)
//        }
        tag.tags.add(profile.id)
        profilesRef.document(tag.id).set(tag)
        notifyDataSetChanged()
        Log.i("adding", "IN EDIT PROFILE, CHANGED PROFILE, NEEDS FIRESTORE UPDATED")
    }




}