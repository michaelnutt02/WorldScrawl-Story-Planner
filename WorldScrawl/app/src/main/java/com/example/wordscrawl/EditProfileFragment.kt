package com.example.wordscrawl

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.profilecategory.Profile
import com.example.wordscrawl.profilecategory.ProfileCardAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class EditProfileFragment() : Fragment() {

    lateinit var adapter: EditProfileAdapter
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
        var view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        adapter = EditProfileAdapter(con)
        recycleView = view.findViewById(R.id.edit_profile_recycler)
        recycleView.layoutManager = LinearLayoutManager(con)
        recycleView.adapter = adapter

        view.findViewById<FloatingActionButton>(R.id.addFAB).setOnClickListener {
            if (adapter != null) {
                adapter.add()
            }
        }
        return view
    }


}