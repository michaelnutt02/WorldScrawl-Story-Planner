package com.example.wordscrawl.SwipeToDeleteCallbacks

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.ProfileDetail
import com.example.wordscrawl.R
import com.example.wordscrawl.editprofile.EditProfileFragment
import com.example.wordscrawl.profilecategory.Profile
import com.example.wordscrawl.profilecategory.ProfileCardAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class ProfileSwipeToDeleteCallback(val adapter:ProfileCardAdapter, val coordinator_layout: View):ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private val detailsRef = FirebaseFirestore
        .getInstance()
        .collection("profile-details")

    private val profilesRef = FirebaseFirestore
        .getInstance()
        .collection("profiles")

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        var savedProfile = adapter.getProfile(viewHolder.adapterPosition)

        val savedDetails:ArrayList<ProfileDetail> = arrayListOf()
        //we do save the correct profile id
        Log.i("adding", "saved profile id is ${savedProfile.id}")
        //get details to save from firestore.

        var listener = detailsRef.whereEqualTo("profileId",savedProfile.id).addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                Log.i("adding", "IN LISTENER")
                if(error != null){
                    Log.i("adding", "ERROR IN LISTENERJ")
                }
                if(snapshot != null){
                    Log.i("adding", "saving a detail for ${savedProfile.name}")
                    savedDetails.addAll(ProfileDetail.fromSnapshots(snapshot))
                }else{
                    Log.i("adding", "snapshot is null")
                }
            }
//        var listener = detailsRef.whereEqualTo("profileId",savedProfile.id).add


        if(direction == ItemTouchHelper.RIGHT){
            adapter.remove(viewHolder.adapterPosition)
            //add snackbar for undo
            var snackbar: Snackbar = Snackbar.make(coordinator_layout, "Profile deleted", Snackbar.LENGTH_LONG)
            snackbar.setAction("UNDO",  View.OnClickListener(){
                adapter.add(savedProfile)
                //find newest added profile in firestore, give it to the edit fragment

                val profileListener = profilesRef
                    .orderBy(Profile.LAST_TOUCHED_KEY, Query.Direction.DESCENDING)
                    .limit(1)
                    .addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                        if(snapshot != null){
                            for(doc in snapshot.documents){
                                savedProfile = Profile.fromSnapshot(doc)
                            }
                        }
                    }
                Log.i("adding", "recovered profile id is ${savedProfile.id}")
                profileListener.remove()

                //add details with the proper id (if this works, we will also want to do this with outlines)
                for(detail in savedDetails){
                    detail.profileId = savedProfile.id
                    detailsRef.add(detail)
                }

                Snackbar.make(coordinator_layout,"Profile is restored", Snackbar.LENGTH_LONG)

            })
            snackbar.show()
        }else{
            //add back food if it was a left swipe
            adapter.remove(viewHolder.adapterPosition)
            adapter.add(savedProfile)
        }


    }


}