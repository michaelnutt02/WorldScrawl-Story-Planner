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

        var layout = inflater.inflate(R.layout.profile_with_picture, container, false)
//        var layout = inflater.inflate(R.layout.fragment_profile, container, false)

//        adapter = ProfileDetailAdapter(con, "szo5if1k5bKSCX1iPtYB")
        adapter = ProfileDetailAdapter(con, profile.id)
        recycleView = layout.findViewById(R.id.profile_detail_recycler)
        recycleView.layoutManager = LinearLayoutManager(con)
        recycleView.setHasFixedSize(true)
        recycleView.adapter = adapter


        val spinner: Spinner = recycleView.findViewById(R.id.add_details_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                con,
                R.array.add_details_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        recycleView.findViewById<FloatingActionButton>(R.id.addFAB).setOnClickListener {
            if (adapter != null) {
                adapter.showAddDialog()
            }
        }


        return layout
    }


}