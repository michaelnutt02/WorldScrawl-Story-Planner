package com.example.wordscrawl.outlines

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.ProfileDetail
import com.example.wordscrawl.R
import com.example.wordscrawl.SwipeToDeleteCallbacks.OutlineSwipeToDeleteCallback
import com.example.wordscrawl.SwipeToDeleteCallbacks.ProfileSwipeToDeleteCallback
import com.example.wordscrawl.WorldsFragment
import com.example.wordscrawl.editprofile.EditProfileAdapter
import com.example.wordscrawl.editprofile.EditProfileFragment
import com.example.wordscrawl.profilecategory.Profile
import com.example.wordscrawl.profiletag.ProfileTagAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class StoryOutlinesFragment() : Fragment() {

    lateinit var adapter: OutlineAdapter
    lateinit var recycleView: RecyclerView
    lateinit var tagAdapter: ProfileTagAdapter
    lateinit var tagRecycleView: RecyclerView

    lateinit var profile:Profile

    lateinit var con:Context

    private var listener : WorldsFragment.OnProfileSelectedListener? = null

    constructor(context: Context, profile: Profile) : this() {

        this.con = context
        this.profile = profile

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_story_outlines, container, false)
        adapter = listener?.let { OutlineAdapter(con, profile, it) }!!

        //outline recycler
        recycleView = view.findViewById(R.id.outline_recycler)
        recycleView.layoutManager = LinearLayoutManager(con)
        recycleView.adapter = adapter

        //code adapted from https://medium.com/@zackcosborn/step-by-step-recyclerview-swipe-to-delete-and-undo-7bbae1fce27e
        recycleView.setAdapter(adapter)
        recycleView.setLayoutManager(LinearLayoutManager(con))
        val itemTouchHelper = ItemTouchHelper(OutlineSwipeToDeleteCallback(adapter, con))
        itemTouchHelper.attachToRecyclerView(recycleView)

        //tag recycler
        tagAdapter = listener?.let { ProfileTagAdapter(con, it, profile) }!!
        tagRecycleView = view.findViewById(R.id.outline_tag_recycler)
        tagRecycleView .layoutManager = LinearLayoutManager(con)
        tagRecycleView .adapter = tagAdapter

        //change name to title of story
        var name = view.findViewById<TextView>(R.id.story_name)
        name.setText(profile.name)

        //enable add button
        view.findViewById<FloatingActionButton>(R.id.addFAB).setOnClickListener{
            //DONE: Make a dialog box that makes them select an outline type
            adapter.showAddDialog()
        }

        view.findViewById<ImageButton>(R.id.editButton).setOnClickListener() {
            val editProfileFragment = EditProfileFragment(con, profile)
            val ft = getActivity()?.supportFragmentManager?.beginTransaction()
            if (ft != null) {
                ft.replace(R.id.fragment_container, editProfileFragment)
                ft.addToBackStack(getString(R.string.skip_edit_page))
//                getActivity()?.supportFragmentManager?.popBackStackImmediate()
                ft.commit()
            }
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is WorldsFragment.OnProfileSelectedListener){
            listener = context
        }else{
            throw RuntimeException(context.toString()+ "must implement OnWorldSelected")
        }

    }

    override fun onDetach() {
        super.onDetach()
        listener = null

    }





}