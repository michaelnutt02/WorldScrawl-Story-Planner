package com.example.worldscrawl

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.provider.Settings
import android.util.Log
import com.example.worldscrawl.profilecategory.Profile
import com.example.worldscrawl.profilecategory.ProfileCardAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class CharactersFragment() : Fragment() {

    lateinit var adapter: ProfileCardAdapter
    lateinit var recycleView: RecyclerView
    lateinit var addButton:FloatingActionButton

    private var listener : WorldsFragment.OnProfileSelectedListener? = null

    private lateinit var con: Context


    constructor(context: Context, addButton: FloatingActionButton) : this() {

        this.con = context
        this.addButton = addButton
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)



    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val layout = inflater.inflate(R.layout.fragment_worlds, container, false)
        //figured out how to do this from https://stackoverflow.com/questions/59864600/recyclerview-still-not-showing-items-on-fragment
        adapter = ProfileCardAdapter(con, listener)
        recycleView = layout.findViewById(R.id.worlds_recycler_view)
        recycleView.layoutManager = LinearLayoutManager(con)
        recycleView.adapter = adapter


        addButton.setOnClickListener{
            var newprofile = Profile("Mary Sue",arrayListOf(), R.drawable.harry_potter)
            adapter.add(newprofile)
            var size = adapter.itemCount
            Log.i("Adding Profile","In Characters, number of profiles are $size")
        }


        return layout


    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        //tie recycleview and adapter here
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is WorldsFragment.OnProfileSelectedListener){
            listener = context
        }else{
            throw RuntimeException(context.toString()+ "must implement OnProfileSelected")
        }

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }




}