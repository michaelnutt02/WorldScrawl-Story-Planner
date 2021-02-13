package com.example.wordscrawl.outlines

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.R
import com.example.wordscrawl.WorldsFragment
import com.example.wordscrawl.editprofile.EditProfileViewHolder
import com.example.wordscrawl.profilecategory.Profile
import com.google.firebase.firestore.*

class OutlineAdapter(var context: Context, val profile:Profile, var listener : WorldsFragment.OnProfileSelectedListener): RecyclerView.Adapter<OutlineViewHolder>() {
    private var outlines: ArrayList<Outline> = arrayListOf()



    private val outlinesRef = FirebaseFirestore
        .getInstance()
        .collection("outlines")


    init {

        //continously add, modify, delete outlines from a story
        outlinesRef
            .orderBy(Profile.LAST_TOUCHED_KEY, Query.Direction.ASCENDING)
            .whereEqualTo("profileId", profile.id)
            .addSnapshotListener{ snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if(error != null){
                    Log.e("ERROR","Listen error $error")
                }
                for(docChange in snapshot!!.documentChanges){
                    val storyOutline = Outline.fromSnapshot(docChange.document)
                    Log.i("adding", "STORIES profile id is ${profile.id} in adapter")
                    when(docChange.type){
                        DocumentChange.Type.ADDED ->{
                            val pos = outlines.size
                            outlines.add(pos, storyOutline)
                            notifyItemInserted(pos)
                        }
                        DocumentChange.Type.REMOVED ->{
                            val pos = outlines.indexOfFirst{storyOutline.id == it.id}
                            outlines.removeAt(pos)
                            notifyItemRemoved(pos)
                        }
                        DocumentChange.Type.MODIFIED ->{
                            val pos = outlines.indexOfFirst{storyOutline.id == it.id}
                            outlines[pos] = storyOutline
                            notifyItemChanged(pos)
                        }
                    }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutlineViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.profile_card, parent, false)


        return OutlineViewHolder(view, this, context)
    }

    override fun onBindViewHolder(holder: OutlineViewHolder, position: Int) {

        holder.bind(outlines[position])
    }

    override fun getItemCount(): Int {
        return outlines.size
    }

    fun selectOutlineAt(position: Int){
        listener.onOutlineSelected(outlines[position])
    }

    fun add(outline:Outline){
        Log.i("adding","STORIES, profile id is ${profile.id} in adapter")
        outlinesRef.add(outline)
    }

    fun remove(position: Int){
        outlinesRef.document(outlines[position].id).delete()
    }

    fun getOutline(position:Int):Outline{
        return outlines[position]
    }

    fun showAddDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.add_outline))
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add, null, false)
        val spinner: Spinner = view.findViewById(R.id.add_details_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                context,
                R.array.add_outlines_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        builder.setView(view)
        builder.setPositiveButton(android.R.string.ok) {_,_->
            add(Outline(type = when(spinner.selectedItem){
                "FREEFORM" -> Outline.TYPE.FREEFORM
                "PINCH" -> Outline.TYPE.PINCH
                "FREYTAG" -> Outline.TYPE.FREYTAG
                else -> Outline.TYPE.FREEFORM
            }, profileId = profile.id))
        }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.create().show()
    }



}