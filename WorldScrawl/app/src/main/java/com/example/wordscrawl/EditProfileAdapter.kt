package com.example.wordscrawl

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.profilecategory.Profile
import com.google.firebase.firestore.*

class EditProfileAdapter(var context: Context, val profileId:String): RecyclerView.Adapter<EditProfileViewHolder>() {
    private var editDetails: ArrayList<ProfileDetail> = arrayListOf()

    private var deletedDetails: ArrayList<ProfileDetail> = arrayListOf()

    private val detailsRef = FirebaseFirestore
            .getInstance()
            .collection("profile-details")

    lateinit var viewHolder: EditProfileViewHolder

    init {
        var isInitialized = false
        //add all existing details of the profile into editDetails
        detailsRef
                .orderBy(Profile.LAST_TOUCHED_KEY, Query.Direction.ASCENDING)
                .whereEqualTo("profileId",profileId)
                .addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                    if(error != null){
                        Log.e("ERROR","Listen error $error")
                    }
                    if(snapshot != null && !isInitialized){
                        editDetails.addAll(ProfileDetail.fromSnapshots(snapshot))
                        notifyDataSetChanged()
                    }else{
                        var emptyEdit = ProfileDetail()
                        editDetails.add(0,emptyEdit)
                        notifyItemInserted(0)
                        Log.i("adding", "No details, profile id is $profileId")
                    }
                    isInitialized = true

                    Log.i("adding", "In loop, profile id is $profileId")
                }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditProfileViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.profile_edit_detail_card, parent, false)

        return EditProfileViewHolder(view, this, context)
    }

    override fun onBindViewHolder(holder: EditProfileViewHolder, position: Int) {
        this.viewHolder = holder
        this.viewHolder.bind(editDetails[position])
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

    fun updateFirestore(){


        //delete all details
        for(detail in deletedDetails){
            detailsRef.document(detail.id).delete()
        }
        //add and modify the rest of the details (make sure to convert to profileDetail)
        for(detail in editDetails){
            this.viewHolder.bind(detail)
            if(detail.profileId.equals("")){
                detail.profileId = profileId
                detailsRef.add(detail)
            }else{
                detailsRef.document(detail.id).set(detail)
            }
        }

    }


}