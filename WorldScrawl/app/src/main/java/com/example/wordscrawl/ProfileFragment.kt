package com.example.wordscrawl

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.profilecategory.Profile
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ProfileFragment() : Fragment() {

    lateinit var adapter: ProfileDetailAdapter
    lateinit var recycleView: RecyclerView
    lateinit var profile:Profile

    private lateinit var con: Context

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

        var layout = inflater.inflate(if(profile.picture == null) R.layout.profile else R.layout.profile_with_picture, container, false)

//        adapter = ProfileDetailAdapter(con, "szo5if1k5bKSCX1iPtYB")
        adapter = ProfileDetailAdapter(con, profile.id)
        recycleView = layout.findViewById(R.id.profile_detail_recycler)
        recycleView.layoutManager = LinearLayoutManager(con)
        recycleView.setHasFixedSize(true)
        recycleView.adapter = adapter


        layout.findViewById<FloatingActionButton>(R.id.addFAB).setOnClickListener {
            if (adapter != null) {
                adapter.showAddDialog()
            }
        }


        return layout
    }


}